package org.rexcellentgames.burningknight.entity.pattern;

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;

public class CircleBulletPattern extends BulletPattern {
	public float radius = 16;

	private int count;

	@Override
	protected void doLogic(BulletProjectile bullet, int i) {
		count = Math.max(count, bullets.size());
		float a = (float) (((float) bullet.i) / count * Math.PI * 2) + t * 2;

		bullet.x = (float) (Math.cos(a) * radius) + x;
		bullet.y = (float) (Math.sin(a) * radius) + y;
	}
}