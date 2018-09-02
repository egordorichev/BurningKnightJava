package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Money;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;

public class MoneyPrinter extends Gun {
	{
		sprite = "item-gun_k";
		hole.x = 12;
		hole.y = 8;
		origin.x = 6;
		origin.y = 4;
		vel = 0.7f;
		damage = 3;
		ammo = Money.class;
	}

	@Override
	protected void modifyBullet(BulletProjectile bullet) {
		super.modifyBullet(bullet);
		bullet.second = false;
		bullet.rotates = true;
	}
}