package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import org.rexcellentgames.burningknight.util.Animation;

public class BrownSkeleton extends Skeleton {
	public static Animation animations = Animation.make("actor-skeleton", "-brown");

	public Animation getAnimation() {
		return animations;
	}

	{
		distance = 64;
		boneSpeed = 200;
		hpMax = 5;
		eight = true;
	}
}