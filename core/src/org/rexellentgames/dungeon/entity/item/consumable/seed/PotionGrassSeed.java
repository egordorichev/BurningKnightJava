package org.rexellentgames.dungeon.entity.item.consumable.seed;

import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.entity.plant.PotionGrass;

public class PotionGrassSeed extends Seed {
	{
		name = "Potiongrass Seed";
		description = "A seed formed like a potion";
		sprite = "item (seed C)";
	}

	@Override
	protected Plant createPlant() {
		return new PotionGrass();
	}
}