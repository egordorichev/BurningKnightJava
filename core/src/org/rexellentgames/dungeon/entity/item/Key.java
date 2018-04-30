package org.rexellentgames.dungeon.entity.item;

import com.badlogic.gdx.audio.Sound;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.util.Random;

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

	@Override
	public void onPickup() {
		super.onPickup();
		Graphics.playSfx("key", 1f, Random.newFloat(0.9f, 1.9f));
	}

	@Override
	public void render() {

	}
}