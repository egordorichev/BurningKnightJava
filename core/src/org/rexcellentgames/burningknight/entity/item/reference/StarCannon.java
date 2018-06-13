package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Star;

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