package org.rexellentgames.dungeon.entity.item.consumable.seed;

import org.rexellentgames.dungeon.entity.plant.Cabbage;
import org.rexellentgames.dungeon.entity.plant.Plant;

public class CabbageSeed extends Seed {
	{
		name = "Cabbage Seed";
		description = "A small seed of cabbage";
		sprite = 130;
	}

	@Override
	protected Plant createPlant() {
		return new Cabbage();
	}
}