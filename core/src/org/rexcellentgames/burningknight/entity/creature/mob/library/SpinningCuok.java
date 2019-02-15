package org.rexcellentgames.burningknight.entity.creature.mob.library;

import org.rexcellentgames.burningknight.util.Animation;

public class SpinningCuok extends Cuok {
	public static Animation animations = Animation.make("actor-cuok", "-green");
	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 8;
	}

	@Override
	public float calcAngle(float a, int num) {
		return (float) (Math.toRadians(Math.round((a - 45f) / 90f) * 90f + 45f) + num * Math.PI / 8);
	}

	@Override
	public boolean recalc() {
		return true;
	}

	@Override
	public int getMax() {
		return 16;
	}
}