package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.entity.creature.buff.SpeedBuff;

public class SpeedPotion extends Potion {
	{
		name = "Speed Potion";
		description = "Sonic uses this, try it!";
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new SpeedBuff());
	}
}