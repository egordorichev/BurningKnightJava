package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Revolver extends Gun {
	{
		accuracy = -3;
		String letter = "a";
		sprite = "item-gun_" + letter;
		name = Locale.get("gun_" + letter);
		description = Locale.get("gun_desc");
		region = Graphics.getTexture(sprite);
		useTime = 0.5f;
	}
}