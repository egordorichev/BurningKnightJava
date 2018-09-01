package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Lightsaber extends Sword {
	{
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

	@Override
	public void generateModifier() {

	}

	protected void setStats() {
		String letter = this.level <= 1 ? "a" : (this.level <= 2 ? "b" : (this.level <= 3 ? "c" : "d"));

		name = Locale.get("lightsaber_" + letter);
		description = Locale.get("lightsaber_desc");
		sprite = "item-lightsaber " + letter.toUpperCase();
		damage = 6;
		useTime = 0.4f;
		region = Graphics.getTexture(sprite);
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}
}