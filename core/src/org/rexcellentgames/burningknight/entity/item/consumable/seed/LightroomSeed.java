package org.rexcellentgames.burningknight.entity.item.consumable.seed;

import org.rexcellentgames.burningknight.entity.plant.Lightroom;
import org.rexcellentgames.burningknight.entity.plant.Plant;

public class LightroomSeed extends Seed {
	{
		name = "Lightroom Seed";
		description = "You can see light coming from the seed";
		sprite = "item-seed_b";
	}

	@Override
	protected Plant createPlant() {
		return new Lightroom();
	}
}