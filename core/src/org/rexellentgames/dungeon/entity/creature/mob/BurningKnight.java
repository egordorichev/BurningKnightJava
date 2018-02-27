package org.rexellentgames.dungeon.entity.creature.mob;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;

public class BurningKnight extends Mob {
	{
		hpMax = 10000;
	}

	private static Animation idle = new Animation(Graphics.sprites, 0.08f, 32, 160, 162,
		164, 166, 168, 170, 172, 174, 176, 178, 180, 182);

	private static Animation hurt = new Animation(Graphics.sprites, 0.1f, 32, 184, 186);
	private static Animation killed = new Animation(Graphics.sprites, 0.1f, 32, 188);

	private PointLight light;

	@Override
	public void init() {
		super.init();

		this.w = 32;
		this.h = 32;
		this.alwaysActive = true;

		this.body = this.createBody(8, 3, 16, 18, BodyDef.BodyType.DynamicBody, true);

		this.light = new PointLight(this.area.getState().getLight(), 128, new Color(0.6f, 0.6f, 1f, 0.8f),
			300, 300, 300);

		this.light.setXray(true);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.vel.mul(0.8f);

		if (this.light != null) {
			this.light.setPosition(this.x + 16, this.y + 16);
		}

		if (this.target == null) {
			this.assignTarget();
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.invt > 0) {
			this.common();
			return;
		}

		if (this.target != null) {
			float dx = this.target.x - this.x - 8;
			float dy = this.target.y - this.y - 8;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d > 1) {
				d /= 3;

				this.vel.x += dx / d;
				this.vel.y += dy / d;
			}
		}

		super.common();
	}

	@Override
	protected void die() {
		super.die();
		this.light.remove(true);
	}

	@Override
	protected void onHurt() {
		this.vel.mul(0f);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		Animation animation;

		if (this.dead) {
			animation = killed;
		} else if (this.invt > 0) {
			animation = hurt;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
	}
}