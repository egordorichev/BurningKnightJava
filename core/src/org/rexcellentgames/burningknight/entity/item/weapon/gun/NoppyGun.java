package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.game.Achievements;

public class NoppyGun extends Gun {
	{
		accuracy = 15f;
		ammoMax = 6;
		reloadRate = 1.5f;
		useTime = 0.2f;
		damage = 1;
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock("UNLOCK_NOPPY");
	}
}