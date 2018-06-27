package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.InvisibilityBuff;

public class InvisibilityPotion extends Potion {
	{
		
		
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new InvisibilityBuff());
	}
}