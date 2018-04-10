package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.RegenerationBuff;

public class RegenerationPotion extends Potion {
	{
		name = Locale.get("regen_potion");
		description = Locale.get("regen_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new RegenerationBuff());
	}
}