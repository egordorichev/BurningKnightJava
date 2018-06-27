package org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars;

import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;

public class LightsaberA extends Sword {
	{
		name = Locale.get("lightsaber_a");
		description = Locale.get("lightsaber_a_desc");
		sprite = "item-lightsaber A";
		damage = 6;
		maxAngle = 360;
		auto = true;
		useTime = 0.2f;
		timeA = 0.2f;
		timeB = 0;
		penetrates = true;
		tr = 0f;
		tg = 0.7f;
		tail = true;
	}
}