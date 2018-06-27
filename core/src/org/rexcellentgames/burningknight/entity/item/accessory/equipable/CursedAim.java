package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class CursedAim extends Equipable {
	{
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.accuracy -= 5;
		this.owner.damageModifier += 0.5;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.accuracy += 5;
		this.owner.damageModifier -= 0.5;
	}
}