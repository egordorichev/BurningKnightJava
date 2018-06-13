package org.rexcellentgames.burningknight.entity.item.consumable.seed;

import org.rexcellentgames.burningknight.entity.plant.Plant;
import org.rexcellentgames.burningknight.entity.plant.PotionGrass;
import org.rexcellentgames.burningknight.entity.plant.Plant;
import org.rexcellentgames.burningknight.entity.plant.PotionGrass;

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