package org.rexcellentgames.burningknight.entity;

import org.rexcellentgames.burningknight.entity.plant.Plant;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.plant.Plant;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class HealthBlocker extends Plant {
	private static Animation animations = Animation.make("veggie", "-heartomato");

	{
		animation = animations.get("growth");
		alwaysActive = true;
		canBurn = false;
	}

	private static AnimationData wilt = animations.get("wilt");

	@Override
	public AnimationData getWiltAnimation() {
		return wilt;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.growProgress = 1f;


		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0.15f, 0, 0, 0.8f, 2.5f, false);
		}
	}
}