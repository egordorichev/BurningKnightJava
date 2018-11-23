package org.rexcellentgames.burningknight.entity.creature.mob.tutorial;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class PurpleSlime extends Slime {
	public static Animation animations = Animation.make("actor-slime", "-purple");

	{
		jump = false;
		hpMax = 1;
	}

	public Animation getAnimation() {
		return animations;
	}

	// dying on tutorial -> shops

	@Override
	protected void onLand() {
		float a = this.getAngleTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);

		BulletProjectile ball = new BulletProjectile();

		ball.velocity = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(30f * 2f);

		ball.x = (float) (this.x + this.w / 2 + Math.cos(a) * 8);
		ball.damage = 4;
		ball.y = (float) (this.y + Math.sin(a) * 8 + 6);
		ball.owner = this;
		ball.bad = true;
		ball.letter = "bullet-nano";

		Dungeon.area.add(ball);

		Camera.push((float) (a - Math.PI), 10);
		this.playSfx("gun_machinegun");
	}
}