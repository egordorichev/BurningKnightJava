package org.rexcellentgames.burningknight.entity.pattern;

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;

public class NanoPattern extends BulletPattern {
	private int count;

	@Override
	protected void doLogic(BulletProjectile bullet, int i) {
		count = Math.max(count, bullets.size());
		boolean fir = bullet.i % 2 == 1;

		float a = (float) (((float) bullet.i) / count * Math.PI * 2) + (t * (fir ? -3 : 3));

		float radius = Math.min(128f, t * 48f * (fir ? 1 : 0.5f));

		bullet.x = (float) (Math.cos(a) * radius) + x;
		bullet.y = (float) (Math.sin(a) * radius) + y;

		if (i % 8 == 0) {
			bullet.done = true;
		}
	}
}