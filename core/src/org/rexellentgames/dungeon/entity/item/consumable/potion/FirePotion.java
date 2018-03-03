package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;

public class FirePotion extends Potion {
	{
		name = "Fire Potion";
		description = "Be careful, inflicts flames";
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new BurningBuff());
	}
}