package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.RegenerationBuff;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.RegenerationBuff;

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