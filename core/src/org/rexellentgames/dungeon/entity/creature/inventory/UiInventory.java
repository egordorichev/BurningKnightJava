package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.game.input.Input;

public class UiInventory extends Entity {
	private Inventory inventory;
	private Item currentSlot;
	private UiSlot[] slots;
	private boolean open = false;

	public UiInventory(Inventory inventory) {
		this.inventory = inventory;
		this.alwaysActive = true;
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

	@Override
	public void update(float dt) {
		if (Input.instance.wasPressed("toggle_inventory")) {
			this.open = !this.open;
		}
	}

	public void renderUi() {
		for (int i = 0; i < (this.open ? 24 : 6); i++) {
			Item item = this.inventory.getSlot(i);
			this.slots[i].render(item == null ? -1 : item.getSprite(), item == null ? 0 : item.getCount());
		}
	}

	public void renderCurrentSlot() {
		if (this.currentSlot != null) {
			Graphics.render(Graphics.items, this.currentSlot.getSprite(), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 8);
			int count = this.currentSlot.getCount();

			if (count > 1) {
				Graphics.small.draw(Graphics.batch, String.valueOf(count), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 4);
			}
		}
	}

	public void setCurrentSlot(Item currentSlot) {
		this.currentSlot = currentSlot;
	}

	public Item getCurrentSlot() {
		return this.currentSlot;
	}
}