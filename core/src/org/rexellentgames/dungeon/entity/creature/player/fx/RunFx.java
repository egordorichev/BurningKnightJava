package org.rexellentgames.dungeon.entity.creature.player.fx;

import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;

public class RunFx extends Entity {
	private static Animation animations = Animation.make("run-fx");
	private AnimationData animation;
	private float t;

	public RunFx(float x, float y) {
		this.x = x;
		this.y = y - 8;
		this.depth = -1;
		this.animation = animations.get("idle");
	}

	@Override
	public void update(float dt) {
		this.t += dt;
		this.animation.update(dt);

		if (this.t >= 0.1f * 7) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y + 8, false);
	}
}