package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.entity.creature.buff.RegenerationBuff;

public class RegenerationPotion extends Potion {
	{
		name = "Regeneration Potion";
		description = "Slowly heals you";
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new RegenerationBuff());
	}
}