package org.rexellentgames.dungeon.entity.plant;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.consumable.potion.Potion;
import org.rexellentgames.dungeon.entity.item.consumable.seed.PotionGrassSeed;
import org.rexellentgames.dungeon.util.Animation;

import java.util.ArrayList;

public class PotionGrass extends Plant {
	private static Animation animations = Animation.make("veggie", "-heartomato");

	{
		animation = animations.get("growth");
	}

	@Override
	public ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		drops.add(new PotionGrassSeed());
		drops.add(Potion.random());

		return drops;
	}
}