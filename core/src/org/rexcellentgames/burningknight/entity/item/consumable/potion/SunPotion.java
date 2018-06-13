package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.LightBuff;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.LightBuff;

public class SunPotion extends Potion {
	{
		name = Locale.get("sun_potion");
		description = Locale.get("sun_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new LightBuff());
	}
}