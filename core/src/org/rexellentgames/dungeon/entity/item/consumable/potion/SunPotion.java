package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.entity.creature.buff.LightBuff;
import org.rexellentgames.dungeon.util.Log;

public class SunPotion extends Potion {
	{
		name = "Sun Potion";
		description = "Increases your shine radius";
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new LightBuff());
	}
}