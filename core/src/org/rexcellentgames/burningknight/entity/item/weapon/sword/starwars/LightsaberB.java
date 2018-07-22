package org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars;

import org.rexcellentgames.burningknight.assets.Locale;

public class LightsaberB extends LightsaberA {
	{
		name = Locale.get("lightsaber_b");
		description = Locale.get("lightsaber_b_desc");
		sprite = "item-lightsaber B";
		damage = 8;
		maxAngle = 360;
		auto = true;
		useTime = 0.2f;
		timeA = 0.2f;
		timeB = 0;
		penetrates = true;
		tr = 1f;
		tg = 0;
		tb = 0.3f;
	}
}