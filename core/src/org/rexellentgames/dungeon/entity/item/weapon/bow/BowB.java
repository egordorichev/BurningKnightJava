package org.rexellentgames.dungeon.entity.item.weapon.bow;

import org.rexellentgames.dungeon.assets.Locale;

public class BowB extends Bow {
	{
		name = Locale.get("bow_b");
		sprite = "item (bow B)";
		description = Locale.get("bow_b_desc");
		useTime = 0.4f;
		damage = 2;
	}
}