package org.rexellentgames.dungeon.entity.item.weapon.sword;

import org.rexellentgames.dungeon.assets.Locale;

public class SwordA extends Sword {
	{
		name = Locale.get("sword_a");
		description = Locale.get("sword_a_desc");
		sprite = "item (sword A)";
		damage = 4;
		useTime = 0.4f;
	}

	@Override
	public String getSfx() {
		return "sword_2";
	}
}