package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.SpeedBuff;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.buff.SpeedBuff;

public class SpeedPotion extends Potion {
	{
		name = Locale.get("speed_potion");
		description = Locale.get("speed_potion_desc");
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new SpeedBuff());
	}
}