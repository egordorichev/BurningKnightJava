package org.rexellentgames.dungeon.entity.item.weapon.sword.starwars;

import org.rexellentgames.dungeon.assets.Locale;

public class LightsaberC extends LightsaberA {
	{
		name = Locale.get("lightsaber_c");
		description = Locale.get("lightsaber_c_desc");
		sprite = "item-lightsaber C";
		damage = 12;
		maxAngle = 360;
		auto = true;
		useTime = 0.2f;
		timeA = 0.2f;
		timeB = 0;
		penetrates = true;
		tr = 1f;
		tg = 0;
		tb = 0.8f;
	}
}