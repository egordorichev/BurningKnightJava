package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Equippable;

public class Dendy extends Equippable {
	{
		name = Locale.get("dendy");
		description = Locale.get("dendy_desc");
		useOnPickup = true;
		sprite = "item-dendy";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifySpeed(10);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifySpeed(-10);
	}
}