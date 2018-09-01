package org.rexcellentgames.burningknight.entity.item.weapon.axe;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class MeatboyAxe extends Axe {
	@Override
	protected void setStats() {
		name = Locale.get("meatboy_axe");
		description = Locale.get("meatboy_axe_desc");
		sprite = "item-meetboy";
		damage = 6;
		speed = 800;
		region = Graphics.getTexture(sprite);
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}
}