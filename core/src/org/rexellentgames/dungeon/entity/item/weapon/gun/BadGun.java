package org.rexellentgames.dungeon.entity.item.weapon.gun;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class BadGun extends Gun {
	{
		name = Locale.get("gun_a");
		description = Locale.get("gun_a_desc");
		sprite = "item (gun A)";
		useTime = 0.5f;
	}

	@Override
	public void use() {
		this.vel = Mob.shotSpeedMod;
		super.use();
	}
}