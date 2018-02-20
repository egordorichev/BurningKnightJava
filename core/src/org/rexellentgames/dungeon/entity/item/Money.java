package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.util.Random;

public class Money extends Item {
	{
		name = "Gold";
		stackable = true;
		// todo: sprite
	}

	@Override
	public Item randomize() {
		this.count = Random.newInt(20, 40);

		return this;
	}
}