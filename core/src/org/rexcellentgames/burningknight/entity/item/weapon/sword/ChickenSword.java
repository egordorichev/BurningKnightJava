package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class ChickenSword extends Sword {
	@Override
	protected void setStats() {
		damage = 10;
		sprite = "item-chicken_sword";
		name = Locale.get("chicken_sword");
		description = Locale.get("chicken_sword_desc");

		region = Graphics.getTexture(sprite);
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}
}