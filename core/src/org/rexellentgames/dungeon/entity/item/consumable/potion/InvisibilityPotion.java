package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.InvisibilityBuff;

public class InvisibilityPotion extends Potion {
	{
		name = Locale.get("invis_potion");
		description = Locale.get("invis_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new InvisibilityBuff());
	}
}