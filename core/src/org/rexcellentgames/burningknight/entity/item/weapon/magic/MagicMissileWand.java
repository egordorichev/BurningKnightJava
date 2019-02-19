package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class MagicMissileWand extends Wand {
	{
		name = Locale.get("magic_missile_wand");
		description = Locale.get("magic_missile_wand_desc");
		sprite = "item-wand_b";
		damage = 4;
		mana = 2;
		projectile = Graphics.getTexture("particle-big");
	}
}