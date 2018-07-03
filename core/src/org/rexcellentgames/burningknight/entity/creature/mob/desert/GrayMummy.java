package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import org.rexcellentgames.burningknight.util.Animation;

public class GrayMummy extends Mummy {
	public static Animation animations = Animation.make("actor-mummy", "-gray");

	{
		speedModifer = 2f;
	}

	@Override
	public Animation getAnimation() {
		return animations;
	}
}