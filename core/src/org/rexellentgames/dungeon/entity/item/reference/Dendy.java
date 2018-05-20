package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;

public class Dendy extends Consumable {
	{
		name = Locale.get("dendy");
		description = Locale.get("dendy_desc");
	}

	@Override
	public void use() {
		super.use();
		this.setCount(this.count - 1);
		this.owner.modifySpeed(10);
	}
}