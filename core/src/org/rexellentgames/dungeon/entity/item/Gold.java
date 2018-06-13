package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.assets.Audio;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class Gold extends Item {
	{
		name = Locale.get("gold");
		stackable = true;
		sprite = "item (coin)";
		autoPickup = true;
		useable = false;
		description = Locale.get("gold_desc");
		identified = true;
	}

	public static ArrayList<ItemHolder> all = new ArrayList<>();

	@Override
	public void onPickup() {
		Audio.playSfx("coin");
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