package org.rexellentgames.dungeon.entity.item.weapon;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;

public class AnotherSword extends Sword {
	{
		name = "Another Sword";
		description = "Looks familiar, huh?";
		sprite = "item (another sword)";
		damage = 6;
		useTime = 0.2f;
	}

	@Override
	protected void onHit(Creature creature) {
		super.onHit(creature);
		creature.addBuff(new BurningBuff());
	}
}