package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class GreenBook extends Equippable {
	{
		name = Locale.get("green_book");
		description = Locale.get("green_book_desc");
		sprite = "item-less_mana_more_damage";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		this.owner.modifyManaMax(-4);
		this.owner.damageModifier += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		this.owner.modifyManaMax(+4);
		this.owner.damageModifier -= 1;
	}
}