package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class StopWatch extends Equipable {
	{
		name = Locale.get("stopwatch");
		description = Locale.get("stopwatch_desc");
		sprite = "item-stop_watch";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		Mob.speedMod = 0.5f;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		Mob.speedMod = 1;
	}
}