package org.rexellentgames.dungeon.entity.item.weapon.bow;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.Item;

public class Arrow extends Item {
	{
		stackable = true;
		useable = false;
	}

	public int damage;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}
}