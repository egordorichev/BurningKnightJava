package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;

public class Explosion extends Entity {
	public static Animation animations = Animation.make("explosion");
	private AnimationData animation = animations.get("idle");
	private boolean flip = Random.chance(50);

	{
		depth = 30;
	}

	public Explosion(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update(float dt) {
		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x - 24, this.y - 24, this.flip);
	}
}