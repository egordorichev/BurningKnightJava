package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

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