package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.claymore.ClaymoreA;

public class DiamondSword extends ClaymoreA {
	{
		name = Locale.get("diamond_sword");
		description = Locale.get("diamond_sword");
		sprite = "item-diamond_sword";
		damage = 8;
		useTime = 0.4f;
	}
}