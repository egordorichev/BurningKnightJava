package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;
import org.rexellentgames.dungeon.util.Log;

public class UiSlot {
	private int x;
	private int y;
	private int id;
	private boolean hovered = false;
	private UiInventory inventory;

	public UiSlot(UiInventory inventory, int id, int x, int y) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.inventory = inventory;
	}

	public void update(float dt) {
		Item item = this.inventory.getInventory().getSlot(this.id);

		if (item != null) {
			item.update(dt);

			if (item.getCount() == 0) {
				this.inventory.getInventory().setSlot(this.id, null);
			}
		}

		if (!this.inventory.isOpen() && this.id >= 6) {
			return;
		}

		this.hovered = CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, this.x, this.y, 24, 24);

		if (this.hovered) {
			this.inventory.hoveredSlot = this.id;
			this.inventory.handled = true;

			if (Input.instance.wasPressed("mouse0")) {
				Item current = this.inventory.getCurrentSlot();
				Item self = this.inventory.getInventory().getSlot(this.id);

				if (current != null && self != null && current.getClass() == self.getClass() && self.isStackable()) {
					current.setCount(current.getCount() + self.getCount());
					this.inventory.getInventory().setSlot(this.id, current);
					this.inventory.setCurrentSlot(null);
				} else {
					this.inventory.setCurrentSlot(self);
					this.inventory.getInventory().setSlot(this.id, current);
				}
			} else if (Input.instance.wasPressed("mouse1")) {
				Item self = this.inventory.getInventory().getSlot(this.id);
				Item current = this.inventory.getCurrentSlot();

				if (self == null || !self.isStackable()) {
					return;
				}

				if (current != null && self.getClass() != current.getClass()) {
					return;
				}

				if (current == null) {
					try {
						current = self.getClass().newInstance();
						current.setCount(0);
						this.inventory.setCurrentSlot(current);
					} catch (Exception e) {
						Dungeon.reportException(e);
					}
				}

				if (self.getCount() == 1) {
					this.inventory.getInventory().setSlot(this.id, null);
				}

				current.setCount(current.getCount() + 1);
				self.setCount(self.getCount() - 1);
			}
		}
	}

	public void render(Item item) {
		if (this.inventory.getActive() == this.id) {
			Graphics.batch.setColor(1, 1, 0.2f, 1);
		} else if (this.hovered) {
			Graphics.batch.setColor(1, 1, 0.5f, 1);
		}

		Graphics.render(Graphics.ui, 64, this.x, this.y, 2, 2);

		if (item != null && item.getDelay() != 0) {
			float delay = item.getDelay();
			float maxDelay = item.getUseTime();

			int w = (int) ((delay / maxDelay) * 24);
			Graphics.batch.setColor(0.5f, 0.5f, 0.5f, 1f);
			Graphics.batch.draw(Graphics.ui, this.x, this.y, w, 32, 0, 32,
				w, 32, false, false);
			Graphics.batch.setColor(1, 1, 1, 1);
		}

		Graphics.batch.setColor(1, 1, 1, 1);

		if (item != null) {
			int sprite = item.getSprite();
			int count = item.getCount();

			if (sprite > -1) {
				Graphics.render(Graphics.items, sprite, this.x + 4, this.y + 12, 1, 1);

				if (count > 1) {
					Graphics.small.draw(Graphics.batch, String.valueOf(count), this.x + 3, this.y + 16);
				}
			}
		}
	}
}