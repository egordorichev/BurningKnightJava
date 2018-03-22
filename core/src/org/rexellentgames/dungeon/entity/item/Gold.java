package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.util.Random;

public class Gold extends Item {
	{
		name = "Gold";
		stackable = true;
		sprite = "item (coin)";
		autoPickup = true;
		useable = false;
		description = "$$$";
	}

	@Override
	public void render(float x, float y, boolean flipped) {

	}

	@Override
	public Item randomize() {
		this.count = Random.newInt(20, 40);

		return this;
	}
}