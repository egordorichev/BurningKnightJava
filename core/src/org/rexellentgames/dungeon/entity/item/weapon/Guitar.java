package org.rexellentgames.dungeon.entity.item.weapon;

import org.rexellentgames.dungeon.assets.Locale;

public class Guitar extends Sword {
	{
		description = Locale.get("guitar_desc");
		name = Locale.get("guitar");
		damage = 11;
		sprite = "item (guitar)";
		useTime = 0.5f;
	}
}