package org.rexcellentgames.burningknight.entity.creature.mob;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.common.DiagonalFly;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class DiagonalShotFly extends DiagonalFly {
	public static Animation animations = Animation.make("actor-fly", "-green");

	public Animation getAnimation() {
		return animations;
	}

	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		lastShot += dt;

		if (lastShot > 2f) {
			shot();
			lastShot = 0;
		}
	}

	@Override
	public void deathEffects() {
		super.deathEffects();
		shot();
	}

	protected void shot() {
		if (Player.instance.room != this.room) {
			return;
		}

		for (int i = 0; i < 8; i++) {
			BulletProjectile ball = new BulletProjectile();

			float a = (float) (i * Math.PI / 4);
			ball.velocity = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(50f * Mob.shotSpeedMod);

			ball.x = (float) (this.x + this.w / 2 + Math.cos(a) * 8);
			ball.y = (float) (this.y + Math.sin(a) * 8 + h / 2);
			ball.damage = 2;
			ball.bad = true;

			ball.letter = "bullet-nano";
			Dungeon.area.add(ball);
		}
	}

	private float lastShot;
}