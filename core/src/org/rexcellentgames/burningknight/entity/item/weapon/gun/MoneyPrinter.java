package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Money;

public class MoneyPrinter extends Gun {
	{
		sprite = "item-gun_k";
		hole.x = 12;
		hole.y = 5;
		origin.x = 6;
		origin.y = 4;
		vel = 0.7f;
		ammo = Money.class;
	}
}