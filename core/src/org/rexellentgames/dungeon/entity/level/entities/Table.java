package org.rexellentgames.dungeon.entity.level.entities;

import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;

public class Table extends SaveableEntity {
	private static Animation animations = Animation.make("throne", "-desk");
	private AnimationData animation;

	{
		w = 42;
		h = 28;
	}

	@Override
	public void init() {
		super.init();
		this.animation = animations.get("idle");
	}

	@Override
	public void render() {
		super.render();
		this.animation.render(this.x, this.y, false);
	}
}