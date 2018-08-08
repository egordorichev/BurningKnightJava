package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import org.rexcellentgames.burningknight.util.Animation;

public class BlueArcheologist extends Archeologist {
	public static Animation animations = Animation.make("actor-archeologist", "-blue");

	@Override
	public Animation getAnimation() {
		return animations;
	}

	{
		triple = true;
		skeletonChance = 10f;
	}
}