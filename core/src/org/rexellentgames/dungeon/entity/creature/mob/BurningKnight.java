package org.rexellentgames.dungeon.entity.creature.mob;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;

public class BurningKnight extends Mob {
	private static Animation idle = new Animation(Graphics.sprites, 0.1f, 32, 160, 162,
		164, 166, 168, 170, 172, 174, 176, 178, 180, 182);

	private PointLight light;

	@Override
	public void init() {
		this.body = this.createBody(8, 3, 16, 18, BodyDef.BodyType.DynamicBody, true);

		this.light = new PointLight(this.area.getState().getLight(), 128, new Color(0.6f, 0.6f, 1f, 0.8f),
			300, 300, 300);

		this.light.setXray(true);
		this.assignTarget();

		this.body.setTransform(this.target.x, this.target.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.vel.mul(0.8f);

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

		this.light.setPosition(this.x + 16, this.y + 16);
		super.common();
	}

	@Override
	public void render() {
		idle.render(this.x, this.y, this.t, this.flipped);
	}
}