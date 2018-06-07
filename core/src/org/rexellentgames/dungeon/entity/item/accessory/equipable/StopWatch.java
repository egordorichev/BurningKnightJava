package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class StopWatch extends Equipable {
	{
		name = Locale.get("stopwatch");
		description = Locale.get("stopwatch_desc");
		sprite = "item-stop_watch";
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