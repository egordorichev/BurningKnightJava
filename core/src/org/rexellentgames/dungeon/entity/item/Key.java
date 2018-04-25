package org.rexellentgames.dungeon.entity.item;

import com.badlogic.gdx.audio.Sound;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;

public class Key extends Item {
	{
		description = Locale.get("key_desc");
		name = Locale.get("key");
		sprite = "item (key)";
		stackable = true;
		useable = false;
		fly = true;
		// autoPickup = true;
	}

	private static Sound pickup = Graphics.getSound("key");

	@Override
	public void onPickup() {
		super.onPickup();
		pickup.play();
	}
}