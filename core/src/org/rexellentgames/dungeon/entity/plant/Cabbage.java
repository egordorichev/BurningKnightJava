package org.rexellentgames.dungeon.entity.plant;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.consumable.seed.CabbageSeed;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class Cabbage extends Plant {
	private static Animation animations = Animation.make("veggie", "-cabbage");

	{
		animation = animations.get("growth");
	}

	@Override
	public ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		drops.add(new CabbageSeed().setCount(Random.newInt(2, 5)));
		drops.add(new org.rexellentgames.dungeon.entity.item.consumable.plant.Cabbage());

		return drops;
	}
}