package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class PenetrationRune extends Equipable {
	{
		name = Locale.get("penetration_rune");
		description = Locale.get("penetration_rune_desc");
		sprite = "item-penetration_stone";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.penetrates = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.penetrates = false;
	}
}