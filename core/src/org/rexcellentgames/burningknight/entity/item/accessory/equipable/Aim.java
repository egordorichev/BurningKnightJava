package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class Aim extends Equipable {
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
	public void onEquip() {
		super.onEquip();
		this.owner.accuracy += 5;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.accuracy -= 5;
	}
}