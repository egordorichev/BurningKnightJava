package org.rexcellentgames.burningknight.entity.plant;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

import java.util.ArrayList;

public class Cabbage extends Plant {
	private static Animation animations = Animation.make("veggie", "-cabbage");
	private static AnimationData wilt = animations.get("wilt");

	@Override
	public AnimationData getWiltAnimation() {
		return wilt;
	}

	{
		animation = animations.get("growth");
	}

	@Override
	public ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		// drops.add(new CabbageSeed().setCount(Random.newInt(2, 5)));
		drops.add(new org.rexcellentgames.burningknight.entity.item.consumable.plant.Cabbage());

		return drops;
	}
}