package org.rexellentgames.dungeon.entity.item.weapon.gun;

import org.rexellentgames.dungeon.assets.Locale;

public class BadGun extends Gun {
	{
		name = Locale.get("gun_a");
		description = Locale.get("gun_a_desc");
		sprite = "item (gun A)";
		useTime = 0.5f;
		vel = 2f;
	}
}