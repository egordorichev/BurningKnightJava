package org.rexellentgames.dungeon.entity.creature.player.fx;

import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;

public class RunFx extends Entity {
	private static Animation animation = Animation.make("run-fx", "idle");
	private float t;

	public RunFx(float x, float y) {
		this.x = x;
		this.y = y - 8;
		this.depth = -1;
	}

	@Override
	public void update(float dt) {
		this.t += dt;
		this.animation.update(dt);

		if (this.t >= 0.05f * 7) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y + 8, false);
	}
}