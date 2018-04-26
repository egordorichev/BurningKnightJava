package org.rexellentgames.dungeon.entity.item.weapon.ranged;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.Item;

public class Arrow extends Item {
	{
		name = Locale.get("arrow");
		description = Locale.get("arrow_desc");
		stackable = true;
		sprite = "item (arrow A)";
		useable = false;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}
}