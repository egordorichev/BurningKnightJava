package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Snowflake;

public class SnowGun extends Gun {
	{
		sprite = "item-gun_n";
		origin.x = 4;
		origin.y = 6;
		hole.x = 6;
		hole.y = 16;
		damage = 1;
		ammo = Snowflake.class;
		damage = 0;
		vel = 0.5f;
	}
}