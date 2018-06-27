package org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars;

import org.rexcellentgames.burningknight.assets.Locale;

public class LightsaberD extends LightsaberA {
	{
		name = Locale.get("lightsaber_d");
		description = Locale.get("lightsaber_d_desc");
		sprite = "item-lightsaber D";
		damage = 14;
		maxAngle = 360;
		auto = true;
		useTime = 0.2f;
		timeA = 0.2f;
		timeB = 0;
		penetrates = true;
		tr = 0;
		tg = 1f;
		tb = 0.3f;
	}
}