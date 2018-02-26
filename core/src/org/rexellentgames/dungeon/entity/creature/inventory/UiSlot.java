package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;

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

	private void update() {

		this.hovered = CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, this.x, this.y, 24, 24);

		if (this.hovered) {
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

	public void render(int sprite, int count) {
		this.update();

		if (this.inventory.getActive() == this.id) {
			Graphics.batch.setColor(1, 1, 0.2f, 1);
		} else if (this.hovered) {
			Graphics.batch.setColor(1, 1, 0.5f, 1);
		}

		Graphics.render(Graphics.ui, 64, this.x, this.y, 2, 2);
		Graphics.batch.setColor(1, 1, 1, 1);

		if (sprite > -1) {
			Graphics.render(Graphics.items, sprite, this.x + 4, this.y + 12, 1, 1);

			if (count > 1) {
				Graphics.small.draw(Graphics.batch, String.valueOf(count), this.x + 3, this.y + 16);
			}
		}
	}
}