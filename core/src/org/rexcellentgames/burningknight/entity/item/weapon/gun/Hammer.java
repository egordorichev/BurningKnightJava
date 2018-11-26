package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Nail;

public class Hammer extends Gun {
	{
		sprite = "item-gun_m";
		hole.x = 16;
		hole.y = 4;
		useTime = 0.1f;
		damage = 2;
		setAccuracy(-5);
		origin.x = 4;
		origin.y = 4;
		ammo = Nail.class;
	}
}