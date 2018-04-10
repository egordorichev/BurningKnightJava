package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.buff.LightBuff;
import org.rexellentgames.dungeon.util.Log;

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