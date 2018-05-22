package org.rexellentgames.dungeon.entity.item.key;

import org.rexellentgames.dungeon.assets.Graphics;
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
		Graphics.playSfx("key");
	}

	@Override
	public void render() {

	}
}