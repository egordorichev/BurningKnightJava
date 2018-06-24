package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class BadGun extends Gun {
	{
		name = Locale.get("gun_a");
		description = Locale.get("gun_a_desc");
		sprite = "item-gun_a";
		useTime = 1.5f;
	}

	@Override
	public void use() {
		this.vel = Mob.shotSpeedMod;
		super.use();
	}
}