package org.rexellentgames.dungeon.entity.item.key;

import org.rexellentgames.dungeon.assets.Audio;
import org.rexellentgames.dungeon.entity.item.Item;

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
		Audio.playSfx("key");
	}

	@Override
	public void render() {

	}
}