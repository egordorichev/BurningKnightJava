package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Shovel extends Claymore {
	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");

		name = Locale.get("shovel_" + letter);
		description = Locale.get("shovel_desc");

		switch (letter) {
			case "a": sprite = "item-bronze_shovel"; break;
			case "b": sprite = "item-iron_shovel"; break;
			case "c": sprite = "item-gold_shovel"; break;
		}

		damage = 4;
		timeA = 0.4f;
		useTime = timeA;
		region = Graphics.getTexture(sprite);
	}
}