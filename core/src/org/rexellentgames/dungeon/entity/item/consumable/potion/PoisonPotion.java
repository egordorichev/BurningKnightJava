package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.entity.creature.buff.PoisonBuff;

public class PoisonPotion extends Potion {
	{
		name = "Potion of Poison";
		description = "Tastes good, at least";
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new PoisonBuff());
	}
}