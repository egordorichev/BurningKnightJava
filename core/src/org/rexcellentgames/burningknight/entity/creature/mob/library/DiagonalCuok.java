package org.rexcellentgames.burningknight.entity.creature.mob.library;

import org.rexcellentgames.burningknight.util.Animation;

public class DiagonalCuok extends Cuok {
	public static Animation animations = Animation.make("actor-cuok", "-yellow");
	public Animation getAnimation() {
		return animations;
	}

	@Override
	public float calcAngle(float a, int num) {
		return (float) Math.toRadians(Math.round((a - 45f) / 90f) * 90f + 45f);
	}
}