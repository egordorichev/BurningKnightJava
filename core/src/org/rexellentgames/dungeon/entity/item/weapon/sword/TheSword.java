package org.rexellentgames.dungeon.entity.item.weapon.sword;

import org.rexellentgames.dungeon.assets.Locale;

public class TheSword extends Sword {
	{
		name = Locale.get("the_sword");
		description = Locale.get("the_sword_desc");
		damage = 601;
		useTime = 0.3f;
		sprite = "item (claymore)";
	}
}