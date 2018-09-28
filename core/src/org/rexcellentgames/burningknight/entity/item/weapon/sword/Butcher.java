package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.game.Achievements;

public class Butcher extends Sword {
	{
		maxAngle = 90;
		timeA = 0.05f;
		timeB = 0.15f;
		useTime = 0.3f;
		timeDelay = useTime - timeA - timeB;
		penetrates = true;
	}

	@Override
	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");

		name = Locale.get("butcher_" + letter);
		description = Locale.get("butcher_desc");
		sprite = "item-butcher_" + letter;
		damage = 5 + level;
		region = Graphics.getTexture(this.sprite);
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock("UNLOCK_BUTCHER");
	}
}