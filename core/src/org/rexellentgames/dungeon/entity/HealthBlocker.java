package org.rexellentgames.dungeon.entity;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;

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