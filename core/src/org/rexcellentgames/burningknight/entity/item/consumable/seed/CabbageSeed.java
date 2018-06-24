package org.rexcellentgames.burningknight.entity.item.consumable.seed;

import org.rexcellentgames.burningknight.entity.plant.Cabbage;
import org.rexcellentgames.burningknight.entity.plant.Plant;

public class CabbageSeed extends Seed {
	{
		name = "Cabbage Seed";
		description = "A small seed of cabbage";
		sprite = "item-seed_a";
	}

	@Override
	protected Plant createPlant() {
		return new Cabbage();
	}
}