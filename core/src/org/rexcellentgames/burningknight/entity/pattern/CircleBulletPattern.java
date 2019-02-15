package org.rexcellentgames.burningknight.entity.pattern;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;

public class CircleBulletPattern extends BulletPattern {
	public float radius = 16;
	public boolean grow;

	private float d;
	private int count;

	@Override
	protected void doLogic(BulletProjectile bullet, int i) {
		if (grow) {
			d = Math.min(radius, d + Gdx.graphics.getDeltaTime() * 2);
		} else {
			d = radius;
		}

		count = Math.max(count, bullets.size());
		float a = (float) (((float) bullet.i) / count * Math.PI * 2) + t * 4;

		bullet.x = (float) (Math.cos(a) * d) + x;
		bullet.y = (float) (Math.sin(a) * d) + y;
	}
}