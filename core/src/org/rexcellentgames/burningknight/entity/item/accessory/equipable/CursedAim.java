package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class CursedAim extends Equipable {
	{
		name = Locale.get("cursed_aim");
		description = Locale.get("cursed_aim_desc");
		sprite = "item-cursed_aim";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.accuracy -= 5;
		this.owner.damageModifier += 0.5;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.accuracy += 5;
		this.owner.damageModifier -= 0.5;
	}
}