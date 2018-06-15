package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.util.Animation;

public class InvisThief extends Thief {
	public static Animation animations = Animation.make("actor-thief", "-black");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 4;
	}

}