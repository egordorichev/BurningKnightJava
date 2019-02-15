package org.rexcellentgames.burningknight.entity.pattern;

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.MathUtils;

public class RectBulletPattern extends BulletPattern {
	public float w = 32;
	public float h = 32;
	public float xm = 16;
	public float ym = 16;

	private int count;

	@Override
	protected void doLogic(BulletProjectile bullet, int i) {
		count = Math.max(count, bullets.size());
		float a = (float) (((float) bullet.i) / count * Math.PI * 2) + t * 4;


		bullet.x = MathUtils.clamp(-xm, xm, (float) (Math.cos(a) * w)) + x;
		bullet.y = MathUtils.clamp(-ym, ym, (float) (Math.sin(a) * h)) + y;
	}
}