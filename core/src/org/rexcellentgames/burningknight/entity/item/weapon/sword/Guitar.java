package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Guitar extends Sword {
	{
		String letter = "a";

		description = Locale.get("guitar_desc");
		name = Locale.get("guitar_" + letter);
		damage = 4;
		sprite = "item-guitar_" + letter;
		useTime = 0.5f;
		region = Graphics.getTexture(this.sprite);
	}
}