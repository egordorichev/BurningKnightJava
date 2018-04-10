package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.SpeedBuff;

public class SpeedPotion extends Potion {
	{
		name = Locale.get("speed_potion");
		description = Locale.get("speed_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new SpeedBuff());
	}
}