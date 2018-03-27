package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Clown extends Mob {
	private static Animation animations = Animation.make("actor-clown");
	private Point point;
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	{
		hpMax = 3;
		speed = 15;

		idle = animations.get("idle");
		run = animations.get("run");
		hurt = animations.get("hurt");
		killed = animations.get("dead");
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(1, 2, 12, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	private void attack() {

	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.dead) {
			super.common();
			return;
		}

		if (!this.noticed) {
			this.vel.mul(0f);
		} else if (this.target != null && !this.target.isDead()) {
			float dx = this.target.x - this.x;
			float dy = this.target.y - this.y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < 20) {
				this.attack();
			} else {
				if (this.t % 0.5 <= 0.017) {
					this.point = this.getCloser(this.target);
				}

				if (this.point != null) {
					dx = this.point.x - this.x;
					dy = this.point.y - this.y;
					d = (float) Math.sqrt(dx * dx + dy * dy);

					if (d < 1) {
						this.x = this.point.x;
						this.y = this.point.y;

						this.point = this.getCloser(this.target);
					} else {
						this.vel.x += dx / d * this.speed;
						this.vel.y += dy / d * this.speed;
					}
				} else {
					this.point = this.getCloser(this.target);
				}
			}
		}

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (v > 9.9) {
			this.become("run");
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}

		if (this.animation != null) {
			this.animation.update(dt);
		}

		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		if (this.falling) {
			this.renderFalling(this.animation);
			return;
		}
		
		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("run")) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.animation.render(this.x, this.y, this.flipped);
	}
}