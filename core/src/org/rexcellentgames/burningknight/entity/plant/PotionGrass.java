package org.rexcellentgames.burningknight.entity.plant;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.consumable.potion.Potion;
import org.rexcellentgames.burningknight.entity.item.consumable.seed.PotionGrassSeed;
import org.rexcellentgames.burningknight.util.Animation;

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