package org.rexcellentgames.burningknight.entity.plant;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class Lightroom extends Plant {
	private static Animation animations = Animation.make("veggie", "-shroom");

	{
		animation = animations.get("growth");
		alwaysActive = true;
	}

	private static AnimationData wilt = animations.get("wilt");

	@Override
	public AnimationData getWiltAnimation() {
		return wilt;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0.15f, 0, 0, 0.8f * Math.max(0.1f, this.growProgress), 2.5f, false);
		}
	}
}