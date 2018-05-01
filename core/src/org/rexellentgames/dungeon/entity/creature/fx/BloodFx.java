package org.rexellentgames.dungeon.entity.creature.fx;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BloodFx extends Entity {
	private static Animation animations = Animation.make("fx-blood");
	private AnimationData animation;
	private Point vel;

	public static void add(Entity entity, int count) {
		add(entity.x, entity.y, entity.w, entity.h, count);
	}

	public static void add(float x, float y, float w, float h, int count) {
		for (int i = 0; i < count; i++) {
			BloodFx fx = new BloodFx();

			fx.x = Random.newFloat(w - 3.5f) + x + 3.5f;
			fx.y = Random.newFloat(h - 3.5f) + y + 3.5f;

			Dungeon.area.add(fx);
		}
	}

	@Override
	public void init() {
		this.vel = new Point(
			Random.newFloat(-1f, 1f),
			Random.newFloat(-1f)
		);

		this.animation = animations.get("idle");
		this.animation.setFrame(Random.newInt(0, 2));
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x;
		this.y += this.vel.y;

		this.vel.x *= 0.96f;
		this.vel.y -= 0.03f;

		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, false, false);
	}
}