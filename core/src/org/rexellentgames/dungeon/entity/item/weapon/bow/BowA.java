package org.rexellentgames.dungeon.entity.item.weapon.bow;

import org.rexellentgames.dungeon.assets.Locale;

public class BowA extends Bow {
	{
		name = Locale.get("bow_a");
		sprite = "item (bow A)";
		description = Locale.get("bow_a_desc");
		useTime = 0.4f;
		damage = 1;
	}
}