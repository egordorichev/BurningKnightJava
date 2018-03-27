package org.rexellentgames.dungeon.entity.item.consumable.seed;

import org.rexellentgames.dungeon.entity.plant.Lightroom;
import org.rexellentgames.dungeon.entity.plant.Plant;

public class LightroomSeed extends Seed {
	{
		name = "Lightroom Seed";
		description = "You can see light coming from the seed";
		sprite = "item (seed B)";
	}

	@Override
	protected Plant createPlant() {
		return new Lightroom();
	}
}