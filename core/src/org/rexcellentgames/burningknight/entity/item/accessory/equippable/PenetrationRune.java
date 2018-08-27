package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class PenetrationRune extends Equippable {
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