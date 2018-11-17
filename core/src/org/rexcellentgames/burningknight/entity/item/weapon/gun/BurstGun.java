package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.game.Achievements;

public class BurstGun extends Gun {
	{
		accuracy = -8f;
		ammoMax = 18;
		reloadRate = 1.5f;
		useTime = 0.1f;
		auto = true;
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock("UNLOCK_BURST");
	}
}