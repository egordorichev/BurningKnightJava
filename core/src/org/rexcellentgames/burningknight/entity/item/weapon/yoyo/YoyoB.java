package org.rexcellentgames.burningknight.entity.item.weapon.yoyo;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class YoyoB extends Yoyo {
	{
		name = Locale.get("yoyo_b");
		description = Locale.get("yoyo_b_desc");
		sprite = "item-yoyo_icon B";
		projectile = Graphics.getTexture("item-yoyo B");
		damage = 9;
		max = 150;
	}
}