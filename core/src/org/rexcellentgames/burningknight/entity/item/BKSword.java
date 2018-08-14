package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SlashSword;

public class BKSword extends SlashSword {
	{
		name = Locale.get("bk_sword");
		description = Locale.get("bk_sword_desc");
		sprite = "item-claymore_a";
		damage = 15;
	}
}