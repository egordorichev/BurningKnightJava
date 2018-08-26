package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;

public class MimicTotem extends Equipable {
	{
		sprite = "item-mimic_totem";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		Mimic.chance -= 100;

		for (Mimic mimic : Mimic.all) {
			mimic.toChest();
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		Mimic.chance += 100;
	}
}