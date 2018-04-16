package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.assets.Locale;

public class Key extends Item {
	{
		description = Locale.get("key_desc");
		name = Locale.get("key");
		sprite = "item (key)";
		stackable = true;
		useable = false;
	}
}