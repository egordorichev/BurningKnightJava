package org.rexellentgames.dungeon.entity.creature.inventory;

import com.badlogic.gdx.Gdx;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Log;

public class UiInventory extends Entity {
	private Inventory inventory;
	private Item currentSlot;
	private UiSlot[] slots;
	private int active = 0;
	private boolean open = false;
	public boolean handled;

	public UiInventory(Inventory inventory) {
		this.inventory = inventory;
		this.alwaysActive = true;

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
				holder.x = (float) Math.floor(Player.instance.x);
				holder.y = (float) Math.floor(Player.instance.y);
				RegularLevel.instance.addSaveable(holder);

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
	}

	public void renderUi() {
		for (int i = 0; i < (this.open ? 24 : 6); i++) {
			Item item = this.inventory.getSlot(i);
			float dt = Gdx.graphics.getDeltaTime();

			if (item != null) {
				item.update(dt);
			}

			this.slots[i].render(item == null ? -1 : item.getSprite(), item == null ? 0 : item.getCount());
		}

		if (!this.handled) {
			if (Input.instance.wasPressed("mouse0")) {
				Item slot = this.inventory.getSlot(this.active);

				if (slot != null && slot.isUseable() && slot.getDelay() == 0) {
					slot.setOwner(Player.instance);
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

	public void renderCurrentSlot() {
		if (this.currentSlot != null) {
			Graphics.render(Graphics.items, this.currentSlot.getSprite(), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 8);
			int count = this.currentSlot.getCount();

			if (count > 1) {
				Graphics.small.draw(Graphics.batch, String.valueOf(count), Input.instance.uiMouse.x + 12, Input.instance.uiMouse.y - 4);
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