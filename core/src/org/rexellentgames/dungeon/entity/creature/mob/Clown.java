package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Clown extends Mob {
	{
		hpMax = 3;
		speed = 15;
	}

	private static Animation idle = Animation.make(Graphics.sprites, 0.08f, 16, 256, 257, 258, 259,
		260, 261, 262, 263);

	private static Animation run = Animation.make(Graphics.sprites, 0.08f, 16, 264, 265, 266, 267,
		268, 269, 270, 271);

	private static Animation hurt = Animation.make(Graphics.sprites, 0.1f, 16, 272, 273);
	private static Animation killed = Animation.make(Graphics.sprites, 1f, 16, 274);

	private Point point;

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

		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);
		Animation animation;

		if (this.dead) {
			animation = killed;
		} else if (this.invt > 0) {
			animation = hurt;
		} else if (this.state.equals("run")) {
			animation = run;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
	}
}