package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.MathUtils;

public class RedArcheologist extends Archeologist {
	public static Animation animations = Animation.make("actor-archeologist", "-red");

	{
		skeletonChance = 0;
		toPlayer = true;
	}

	@Override
	public void generatePrefix() {

	}

	@Override
	public Animation getAnimation() {
		return animations;
	}

	@Override
	public void render() {
		if (this.target != null) {
			float d = this.getDistanceTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);
			float tar = (128f - d) / 128;
			this.a = MathUtils.clamp(0.1f, 1, tar);
		}

		super.render();
	}
}