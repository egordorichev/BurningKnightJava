package org.rexcellentgames.burningknight.entity.creature.mob.library;

import org.rexcellentgames.burningknight.util.Animation;

public class FourSideCuok extends Cuok {
	public static Animation animations = Animation.make("actor-cuok", "-cyan");
	public Animation getAnimation() {
		return animations;
	}

	@Override
	public void spawnBullet(float a) {
		for (int i = 0; i < 4; i++) {
			super.spawnBullet((float) (a + Math.PI / 2 * i));
		}
	}

	@Override
	public int getMax() {
		return 3;
	}
}