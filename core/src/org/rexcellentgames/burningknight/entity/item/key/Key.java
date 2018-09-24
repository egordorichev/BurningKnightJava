package org.rexcellentgames.burningknight.entity.item.key;

import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Key extends Item {
	{
		stackable = true;
		useable = false;
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