package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.DefenseBuff;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.DefenseBuff;

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