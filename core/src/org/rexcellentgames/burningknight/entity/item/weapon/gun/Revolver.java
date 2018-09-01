package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Revolver extends Gun {
	@Override
	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");
		sprite = "item-gun_" + letter;
		name = Locale.get("revolver_" + letter);
		description = Locale.get("revolver_desc");
		region = Graphics.getTexture(sprite);
	}

	@Override
	public int getMaxLevel() {
		return 7;
	}
}