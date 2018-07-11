package org.rexcellentgames.burningknight.entity.item.weapon.yoyo;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class YoyoA extends Yoyo {
	{
		name = Locale.get("yoyo_a");
		description = Locale.get("yoyo_a_desc");
		sprite = "item-yoyo_icon A";
		damage = 6;
		projectile = Graphics.getTexture("item-yoyo A");
	}
}