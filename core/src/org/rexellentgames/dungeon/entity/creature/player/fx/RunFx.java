package org.rexellentgames.dungeon.entity.creature.player.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;

public class RunFx extends Entity {
	private static Animation animations = Animation.make("run-fx");
	private AnimationData animation;

	public RunFx(float x, float y) {
		this.x = x;
		this.y = y - 8;
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
		TextureRegion region = this.animation.getCurrent().frame;
		this.animation.render(this.x, this.y + 8, false, false, region.getRegionWidth() / 2, region.getRegionHeight() / 2, 0,
			0.5f, 0.5f, false);
	}
}