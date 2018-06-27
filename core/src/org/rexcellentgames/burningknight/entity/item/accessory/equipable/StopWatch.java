package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class StopWatch extends Equipable {
	{
	}

	@Override
	public void onEquip() {
		super.onEquip();
		Mob.speedMod = 0.5f;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Mob.speedMod = 1;
	}
}