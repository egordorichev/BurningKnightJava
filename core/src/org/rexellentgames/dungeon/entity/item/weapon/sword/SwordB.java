package org.rexellentgames.dungeon.entity.item.weapon.sword;

import org.rexellentgames.dungeon.assets.Locale;

public class SwordB extends Sword {
	{
		name = Locale.get("sword_b");
		description = Locale.get("sword_b_desc");
		sprite = "item (sword B)";
		damage = 6;
		useTime = 0.3f;
	}
}