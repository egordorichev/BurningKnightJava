package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class Knight extends Mob {
	{
		hpMax = 10;
	}

	private static Animation idle = new Animation(Graphics.sprites, 0.08f, 16, 224, 225, 226, 227,
		228, 229, 230, 231);

	private static Animation run = new Animation(Graphics.sprites, 0.08f, 16, 232, 233, 234, 235,
		236, 237, 238, 239);

	private static Animation hurt = new Animation(Graphics.sprites, 0.1f, 16, 240, 241);
	private static Animation killed = new Animation(Graphics.sprites, 1f, 16, 242);

	private Point point;

	@Override
	public void init() {
		super.init();

		this.speed = 10;
		this.body = this.createBody(1, 2, 12, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.vel.mul(0.8f);

		if (this.dead) {
			super.common();
			return;
		}

		if (this.target != null) {
			float dx = this.target.x - this.x;
			float dy = this.target.y - this.y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < 16) {
				// attack
			} else {
				if (this.t % 1 <= 0.017) {
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
		} else {
			this.assignTarget();
		}

		// todo: cap
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
		super.render();

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