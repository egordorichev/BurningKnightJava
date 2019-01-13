package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.util.Animation;

public class BossThief extends Thief {
	public static Animation animations = Animation.make("actor-thief", "-green");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 20;
	}
}