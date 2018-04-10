package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.DefenseBuff;

public class DefensePotion extends Potion {
	{
		name = Locale.get("defense_potion");
		description = Locale.get("defense_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new DefenseBuff());
	}
}