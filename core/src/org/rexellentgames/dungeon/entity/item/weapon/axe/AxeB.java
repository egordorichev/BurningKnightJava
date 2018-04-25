package org.rexellentgames.dungeon.entity.item.weapon.axe;

import org.rexellentgames.dungeon.assets.Locale;

public class AxeB extends Axe {
	{
		name = Locale.get("axe_b");
		description = Locale.get("axe_b_desc");
		damage = 5;
		penetrates = true;
		speed = 600;
		sprite = "item (axe B)";
	}
}