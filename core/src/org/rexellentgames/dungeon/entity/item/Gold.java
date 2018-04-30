package org.rexellentgames.dungeon.entity.item;

import com.badlogic.gdx.audio.Sound;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Random;

public class Gold extends Item {
	{
		name = "Gold";
		stackable = true;
		sprite = "item (coin)";
		autoPickup = true;
		useable = false;
		description = "$$$";
		identified = true;
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Graphics.playSfx("coin", 1f, Random.newFloat(0.9f, 1.9f));
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}

	@Override
	public void generate() {
		super.generate();
		this.count = Random.newInt(10, 20);
	}
}