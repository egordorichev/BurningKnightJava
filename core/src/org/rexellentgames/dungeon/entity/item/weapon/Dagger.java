package org.rexellentgames.dungeon.entity.item.weapon;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.SlowBuff;

public class Dagger extends Sword {
	{
		description = Locale.get("dagger");
		damage = 4;
		name = Locale.get("dagger_desc");
		useTime = 0.3f;
		sprite = "item (dagger)";
	}
}