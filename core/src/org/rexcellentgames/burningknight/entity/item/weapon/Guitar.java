package org.rexcellentgames.burningknight.entity.item.weapon;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;

public class Guitar extends Sword {
	{
		description = Locale.get("guitar_desc");
		name = Locale.get("guitar");
		damage = 4;
		sprite = "item-guitar_a";
		useTime = 0.5f;
	}
}