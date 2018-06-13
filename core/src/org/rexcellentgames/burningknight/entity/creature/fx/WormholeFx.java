package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class WormholeFx extends Entity {
	private Point vel = new Point();
	public static ArrayList<Suckable> suck = new ArrayList<>();
	private static Animation animations = Animation.make("wormhole");
	private AnimationData animation = animations.get("idle");
	private float t;

	@Override
	public void init() {
		super.init();

		this.alwaysActive = true;
		this.depth = -1;

		float dx = Input.instance.worldMouse.x - this.x;
		float dy = Input.instance.worldMouse.y - this.y;

		float a = (float) Math.atan2(dy, dx);

		this.vel.x = (float) (Math.cos(a) * 2f);
		this.vel.y = (float) (Math.sin(a) * 2f);
	}

	@Override
	public void render() {
		animation.render(this.x, this.y, false);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.t >= 20f) {
			this.done = true;
		}

		this.vel.mul(0.9f);
		this.x += this.vel.x;
		this.y += this.vel.y;

		this.animation.update(dt);

		for (Suckable entity : suck) {
			Body body = entity.getBody();

			float dx = this.x + 8 - body.getPosition().x - 5;
			float dy = this.y + 8 - body.getPosition().y - 5;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d >= 1f && d <= 100f) {
				Vector2 vel = body.getLinearVelocity();
				float sd = (float) Math.sqrt(d) / 2;

				vel.x += dx / sd;
				vel.y += dy / sd;

				body.setLinearVelocity(vel);
			}
		}
	}

	interface Suckable {
		Body getBody();
	}
}