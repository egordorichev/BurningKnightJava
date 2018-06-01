package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

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

	public static ArrayList<ItemHolder> all = new ArrayList<>();

	@Override
	public void onPickup() {
		Graphics.playSfx("coin");
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