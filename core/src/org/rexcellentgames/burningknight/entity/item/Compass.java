package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Equippable;

public class Compass extends Equippable {
	{
		description = Locale.get("compass_desc");
		name = Locale.get("compass");
		sprite = "item-compass";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.seePath = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.seePath = false;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}