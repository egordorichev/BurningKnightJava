package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.claymore.Claymore;

public class DiamondSword extends Claymore {
	@Override
	protected void setStats() {
		name = Locale.get("diamond_sword");
		description = Locale.get("diamond_sword");
		sprite = "item-diamond_sword";
		damage = 8;
		useTime = 0.4f;
		region = Graphics.getTexture(sprite);
	}
}