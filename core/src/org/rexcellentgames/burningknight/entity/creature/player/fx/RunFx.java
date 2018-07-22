package org.rexcellentgames.burningknight.entity.creature.player.fx;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class RunFx extends Entity {
	private static Animation animations = Animation.make("fx-run");
	private AnimationData animation;

	public RunFx(float x, float y) {
		this.x = x;
		this.y = y;
		this.depth = -1;
		this.animation = animations.get("idle");
	}

	@Override
	public void update(float dt) {
		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x + 5, this.y + 5, false, false, 5, 5, 0,
			0.5f, 0.5f);
	}
}