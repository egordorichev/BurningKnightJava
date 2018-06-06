package org.rexellentgames.dungeon.entity.item.weapon.sword;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.weapon.sword.claymore.ClaymoreA;

public class Shovel extends ClaymoreA {
	{
		damage = 12;
		sprite = "item-shovel";
		name = Locale.get("shovel");
		description = Locale.get("shovel_desc");
	}
}