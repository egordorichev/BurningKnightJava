package org.rexellentgames.dungeon.entity.plant;

import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class PlantFx extends Entity {
	private static Animation animations = Animation.make("plant-fx");
	private AnimationData animation;
	private Point vel;

	@Override
	public void init() {
		super.init();

		this.animation = animations.get("idle");
		this.vel = new Point();

		float a = Random.newFloat((float) (Math.PI * 2));
		float s = Random.newFloat(0.5f, 2f);

		this.vel.x = (float) (Math.cos(a) * s);
		this.vel.y = (float) (Math.sin(a) * s);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.vel.mul(0.9f);
		this.x += this.vel.x;
		this.y += this.vel.y;

		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x - 4, this.y - 4, false);
	}
}