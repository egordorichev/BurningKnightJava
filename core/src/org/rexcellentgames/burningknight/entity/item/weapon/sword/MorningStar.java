package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class MorningStar extends Sword {
	{
		useTime = 0.7f;
		timeDelay = useTime - timeA - timeB;
		maxAngle = 180;
	}

	@Override
	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");

		sprite = "item-morning_" + letter;
		name = Locale.get("morning_" + letter);
		description = Locale.get("morning_desc");
		damage = 5;
		minDamage = 5;
		region = Graphics.getTexture(this.sprite);
	}
}