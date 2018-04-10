package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.PoisonBuff;

public class PoisonPotion extends Potion {
	{
		name = Locale.get("poison_potion");
		description = Locale.get("poison_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new PoisonBuff());
	}
}