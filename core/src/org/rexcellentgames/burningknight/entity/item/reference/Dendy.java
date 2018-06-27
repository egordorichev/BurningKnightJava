package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class Dendy extends Consumable {
	{
		name = Locale.get("dendy");
		description = Locale.get("dendy_desc");
		useOnPickup = true;
		sprite = "item-dendy";
	}

	@Override
	public void use() {
		super.use();
		this.setCount(this.count - 1);
		this.owner.modifySpeed(3);
	}
}