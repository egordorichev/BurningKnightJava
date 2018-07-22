package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;

public class MimicTotem extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		Mimic.chance -= 100;

		for (Mimic mimic : Mimic.all) {
			mimic.toChest();
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Mimic.chance += 100;
	}
}