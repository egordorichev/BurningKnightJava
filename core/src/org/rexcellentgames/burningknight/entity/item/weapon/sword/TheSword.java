package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class TheSword extends Sword {
	@Override
	protected void setStats() {
		super.setStats();
		name = Locale.get("the_sword");
		description = Locale.get("the_sword_desc");
		damage = 601;
		useTime = 0.3f;
		sprite = "item-claymore_a";
		region = Graphics.getTexture(sprite);
	}
}