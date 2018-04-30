package org.rexellentgames.dungeon.entity.item.key;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.util.Random;

public class Key extends Item {
	{
		stackable = true;
		useable = false;
		fly = true;
		identified = true;
		// autoPickup = true;
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Graphics.playSfx("key", 1f, Random.newFloat(0.9f, 1.1f));
	}

	@Override
	public void render() {

	}
}