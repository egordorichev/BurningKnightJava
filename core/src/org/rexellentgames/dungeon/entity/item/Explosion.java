package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
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
	public void init() {
		super.init();
		Camera.instance.shake(10f);
	}

	@Override
	public void update(float dt) {
		Dungeon.level.addLightInRadius(this.x, this.y, 1f, 0.7f, 0f, 0.8f, 5f, true);

		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x - 24, this.y - 24, this.flip);
	}
}