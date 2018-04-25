package org.rexellentgames.dungeon.entity.item.weapon.axe;

import org.rexellentgames.dungeon.assets.Locale;

public class AxeD extends Axe {
	{
		name = Locale.get("axe_d");
		description = Locale.get("axe_d_desc");
		damage = 20;
		penetrates = true;
		sprite = "item (axe D)";
		speed = 1000;
	}
}