package org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Part extends Entity {
	private static Animation animations = Animation.make("fx-part");
	public AnimationData animation;
	public Point vel;
	public float speed = 1f;
	public boolean shadow = true;

	@Override
	public void init() {
		this.alwaysActive = true;

		if (this.vel == null) {
			this.vel = new Point(
				Random.newFloat(-1f, 1f),
				Random.newFloat(-1f, 1f)
			);
		}

		this.vel.mul(60);

		if (this.animation == null) {
			this.animation = animations.get("idle");
			this.animation.setFrame(Random.newInt(0, 2));
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.mul(0.95f);

		if (this.animation.update(dt / this.speed)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, false, false);
	}
}