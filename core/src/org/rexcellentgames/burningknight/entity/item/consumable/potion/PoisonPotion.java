package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;

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