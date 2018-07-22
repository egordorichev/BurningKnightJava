package org.rexcellentgames.burningknight.entity.item.weapon.yoyo;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class YoyoC extends Yoyo {
	{
		name = Locale.get("yoyo_c");
		description = Locale.get("yoyo_c_desc");
		sprite = "item-yoyo_icon C";
		projectile = Graphics.getTexture("item-yoyo C");
		damage = 13;
		max = 200;
	}
}