package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class DewVial extends Equipable {
	{
		name = Locale.get("dew_vial");
		description = Locale.get("dew_vial_desc");
		sprite = "item-dew_vial";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.healOnEnter = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.healOnEnter = false;
	}
}