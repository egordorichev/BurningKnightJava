package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.Item;

public class UiInventory extends Entity {
	private Inventory inventory;

	public UiInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public void update(float dt) {

	}

	public void renderUi() {
		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 4; y++) {
				int xx = x * 25 + 1;
				int yy = y * 25 - 7;
				int i = x + y * 6;

				Graphics.render(Graphics.ui, 64, xx, yy, 2, 2);

				Item slot = this.inventory.getSlot(i);

				if (slot != null) {
					// todo: render count
					Graphics.render(Graphics.items, slot.getSprite(), xx + 4, yy + 12, 1, 1);
				}
			}
		}
	}
}