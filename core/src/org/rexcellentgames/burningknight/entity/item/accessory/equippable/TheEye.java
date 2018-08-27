package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class TheEye extends Equippable {
	{
		name = Locale.get("the_eye");
		description = Locale.get("the_eye_desc");
		sprite = "item-the_eye";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		Mob.shotSpeedMod = 0.5f;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		Mob.shotSpeedMod = 1;
	}
}