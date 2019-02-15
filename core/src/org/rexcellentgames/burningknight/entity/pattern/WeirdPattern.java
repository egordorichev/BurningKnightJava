package org.rexcellentgames.burningknight.entity.pattern;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;

public class WeirdPattern extends BulletPattern {
	private int count;
	private float d;

	@Override
	protected void doLogic(BulletProjectile bullet, int i) {
		i = bullet.i;
		d = Math.min(64, d + Gdx.graphics.getDeltaTime() * 2f);

		count = Math.max(count, bullets.size());
		float t = this.t * 3f;
		float a = (float) (((float) i) / count * Math.PI * 2) + (t);

		float p = (float) (Math.cos(t * 0.05f * Math.PI * 2) * 0.5f + 0.5f);
		float d = (float) (Math.cos((i * (1f / count * 2) + t * 0.05f) * Math.PI * 2) * this.d * p + (1 - p) * 32);

		bullet.x = (float) (Math.cos(a) * d) + x;
		bullet.y = (float) (Math.sin(a) * d) + y;
	}
}