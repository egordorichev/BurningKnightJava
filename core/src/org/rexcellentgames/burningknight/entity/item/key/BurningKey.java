package org.rexcellentgames.burningknight.entity.item.key;

import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;

public class BurningKey extends Key {
	{
		autoPickup = false;
		sprite = "item-burning_key";
	}

	@Override
	public void onPickup() {
		super.onPickup();

		if (BurningKnight.instance != null && !BurningKnight.instance.pickedKey) {
			BurningKnight.instance.pickedKey = true;
			BurningKnight.instance.become("appear");
		}
	}
}