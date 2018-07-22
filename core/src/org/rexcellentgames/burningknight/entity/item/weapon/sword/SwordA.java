package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Locale;

public class SwordA extends Sword {
	{
		name = Locale.get("sword_a");
		description = Locale.get("sword_a_desc");
		sprite = "item-sword_a";
		damage = 4;
		useTime = 0.4f;
	}

	@Override
	public String getSfx() {
		return "sword_2";
	}
}