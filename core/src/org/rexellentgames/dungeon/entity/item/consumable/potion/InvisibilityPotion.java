package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.entity.creature.buff.InvisibilityBuff;

public class InvisibilityPotion extends Potion {
	{
		name = "Invisibility Potion";
		description = "Where did he go?";
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new InvisibilityBuff());
	}
}