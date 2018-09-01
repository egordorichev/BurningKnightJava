package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class BlueShovel extends Claymore {
	@Override
	protected void setStats() {
		damage = 12;
		sprite = "item-shovel";
		name = Locale.get("blue_shovel");
		description = Locale.get("blue_shovel_desc");
		region = Graphics.getTexture(sprite);
	}
}