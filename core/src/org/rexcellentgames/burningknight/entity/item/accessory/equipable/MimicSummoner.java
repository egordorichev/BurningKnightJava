package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;

public class MimicSummoner extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		Mimic.chance += 100;

		for (Chest chest : Chest.all) {
			chest.toMimic();
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Mimic.chance -= 100;
	}
}