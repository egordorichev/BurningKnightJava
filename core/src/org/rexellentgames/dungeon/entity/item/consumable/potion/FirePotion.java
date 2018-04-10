package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;

public class FirePotion extends Potion {
	{
		name = Locale.get("fire_potion");
		description = Locale.get("fire_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new BurningBuff());
	}
}