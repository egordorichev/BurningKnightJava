package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.Dungeon;

public class VVVVV extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();

		Dungeon.flip = !Dungeon.flip;
	}

	@Override
	public void onUnequip() {
		super.onEquip();

		Dungeon.flip = !Dungeon.flip;
	}
}