package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.ui.UiEntity;
import org.rexellentgames.dungeon.util.MathUtils;

public class UiInventory extends UiEntity {
	private Inventory inventory;
	private Item currentSlot;
	private UiSlot[] slots;
	private int active = 0;
	private boolean open = false;
	public boolean handled;
	public int hoveredSlot = -1;

	public UiInventory(Inventory inventory) {
		this.inventory = inventory;

		Player.instance.setUi(this);
	}

	@Override
	public void init() {
		this.slots = new UiSlot[24];

		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = new UiSlot(this, i, i % 6 * 25 + 1, (int) (Math.floor(i / 6) * 25 - 7));
		}
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public int getActive() {
		return this.active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public boolean isOpen() {
		return this.open;
	}

	@Override
	public void update(float dt) {
		this.handled = false;

		if (Input.instance.wasPressed("toggle_inventory")) {
			this.open = !this.open;
		}

		if (Input.instance.wasPressed("scroll")) {
			this.active = (this.active + Input.instance.getAmount()) % 5;

			if (this.active == -1) {
				this.active = 5;
			}
		}

		if (Input.instance.wasPressed("drop_item") && !this.open) {
			Item slot = this.inventory.getSlot(this.active);

			if (slot != null && !slot.hasAutoPickup()) {
				ItemHolder holder = new ItemHolder();

				holder.setItem(slot);
				holder.x = (float) Math.floor(Player.instance.x + Player.instance.w / 2);
				holder.y = (float) Math.floor(Player.instance.y + Player.instance.h / 2);
				Dungeon.level.addSaveable(holder);

				this.inventory.setSlot(this.active, null);
				this.area.add(holder);
			}
		}

		if (Input.instance.wasPressed("1")) {
			this.active = 0;
		}

		if (Input.instance.wasPressed("2")) {
			this.active = 1;
		}

		if (Input.instance.wasPressed("3")) {
			this.active = 2;
		}

		if (Input.instance.wasPressed("4")) {
			this.active = 3;
		}

		if (Input.instance.wasPressed("5")) {
			this.active = 4;
		}

		if (Input.instance.wasPressed("6")) {
			this.active = 5;
		}

		for (UiSlot slot : this.slots) {
			slot.update(dt);
		}

		if (!this.handled && !Player.instance.isDead()) {
			if (Input.instance.wasPressed("mouse0")) {
				Item slot = this.inventory.getSlot(this.active);

				if (slot != null && slot.isUseable() && slot.getDelay() == 0) {
					slot.use();
				}
			} else if (Input.instance.wasPressed("mouse1")) {
				Item slot = this.inventory.getSlot(this.active);

				if (slot != null && slot.isUseable() && slot.getDelay() == 0) {
					slot.setOwner(Player.instance);
					slot.secondUse();
				}
			}
		}
	}

	@Override
	public void render() {
		for (int i = 0; i < (this.open ? 24 : 6); i++) {
			Item item = this.inventory.getSlot(i);
			this.slots[i].render(item);
		}
	}

	public void renderCurrentSlot() {
		if (this.currentSlot != null) {
			Graphics.render(Graphics.items, this.currentSlot.getSprite(), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 8);
			int count = this.currentSlot.getCount();

			if (count > 1) {
				Graphics.small.draw(Graphics.batch, String.valueOf(count), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 4);
			}
		} else if (this.open && this.hoveredSlot != -1) {
			Item item = this.inventory.getSlot(this.hoveredSlot);

			if (item != null) {
				String info = item.buildInfo().toString();
				Graphics.layout.setText(Graphics.small, info);
				Graphics.small.draw(Graphics.batch, info,
					MathUtils.clamp(1, Display.GAME_WIDTH - 1, (int) Input.instance.uiMouse.x + 12),
					MathUtils.clamp((int) Graphics.layout.height + 1, Display.GAME_HEIGHT - 1, (int) Input.instance.uiMouse.y + 2));
				this.hoveredSlot = -1;
			}
		}
	}

	public void renderOnPlayer(Player player) {
		Item slot = this.inventory.getSlot(this.active);

		if (slot != null) {
			slot.render(player.x, player.y, player.isFlipped());
		}
	}

	public void setCurrentSlot(Item currentSlot) {
		this.currentSlot = currentSlot;
	}

	public Item getCurrentSlot() {
		return this.currentSlot;
	}
}