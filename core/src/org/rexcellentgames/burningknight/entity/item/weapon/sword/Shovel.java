package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.claymore.Claymore;

public class Shovel extends Claymore {
	{
		damage = 12;
		sprite = "item-shovel";
		name = Locale.get("shovel");
		description = Locale.get("shovel_desc");
	}
}