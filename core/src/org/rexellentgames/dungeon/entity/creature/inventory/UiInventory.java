package org.rexellentgames.dungeon.entity.creature.inventory;

import org.rexellentgames.dungeon.entity.Entity;

public class UiInventory extends Entity {
	private Inventory inventory;

	public UiInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public void update(float dt) {

	}

	public void renderUi() {

	}
}