package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class TheEye extends Equipable {
	{
		name = Locale.get("the_eye");
		description = Locale.get("the_eye_desc");
		sprite = "item-the_eye";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		Mob.shotSpeedMod = 0.5f;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Mob.shotSpeedMod = 1;
	}
}