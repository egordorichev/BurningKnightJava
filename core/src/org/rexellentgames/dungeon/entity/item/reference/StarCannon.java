package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.weapon.gun.Gun;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Star;

public class StarCannon extends Gun {
	{
		sprite = "item (star_cannon)";
		damage = 4;
		ammo = Star.class;
		accuracy = 1f;
		penetrates = true;
		name = Locale.get("star_cannon");
		description = Locale.get("star_cannon_desc");
	}
}