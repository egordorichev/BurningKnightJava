package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class Aim extends Equippable {
	{
		name = Locale.get("aim");
		description = Locale.get("aim_desc");
		sprite = "item-aim";
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.accuracy += 5;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.accuracy -= 5;
	}
}