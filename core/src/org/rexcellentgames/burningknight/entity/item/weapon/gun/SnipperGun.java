package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class SnipperGun extends Gun {
	{
		bulletSprite = "bullet-rekt";
	}

	@Override
	public void use() {
		if (showRedLine) {
			return;
		}

		if ((!(this.owner instanceof Mob) && ammoLeft <= 0) || this.delay > 0) {
			return;
		}

		showRedLine = true;

		Tween.to(new Tween.Task(0, 1.5f) {
			@Override
			public void onEnd() {
				realUse();
				showRedLine = false;
			}
		});
	}

	protected void realUse() {
		super.use();
	}

	@Override
	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);
		float v = 0.08f;

		this.sendBullet(a);
		this.sendBullet(a + v);
		this.sendBullet(a - v);
	}
}