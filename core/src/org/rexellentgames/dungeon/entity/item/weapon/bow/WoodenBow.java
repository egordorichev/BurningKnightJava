package org.rexellentgames.dungeon.entity.item.weapon.bow;

import org.rexellentgames.dungeon.assets.Locale;

public class WoodenBow extends Bow {
	{
		name = Locale.get("bow");
		sprite = "item (bow A)";
		description = Locale.get("bow_desc");
		useTime = 0.4f;
		damage = 6;
	}
}