package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.entity.creature.buff.DefenseBuff;

public class DefensePotion extends Potion {
	{
		name = "Defense Potion";
		description = "Makes your skin iron for a bit";
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new DefenseBuff());
	}
}