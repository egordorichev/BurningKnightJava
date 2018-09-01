package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Pickaxe extends Sword {
	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : (this.level == 7 ? "d" : "c"));

		name = Locale.get("pickaxe_" + letter);
		description = Locale.get("pickaxe_desc");

		switch (letter) {
			case "a": sprite = "item-bronze_pickaxe"; break;
			case "b": sprite = "item-iron_pickaxe"; break;
			case "c": sprite = "item-gold_pickaxe"; break;
			case "d": sprite = "item-diamond_pickaxe"; break;
		}

		damage = 4;
		useTime = 0.4f;
		region = Graphics.getTexture(sprite);
		damage = 5;
	}
}