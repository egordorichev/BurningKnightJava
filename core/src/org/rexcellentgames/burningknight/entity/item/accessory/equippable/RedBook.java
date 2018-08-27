package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class RedBook extends Equippable {
	{
		name = Locale.get("red_book");
		description = Locale.get("red_book_desc");
		sprite = "item-life_regen_becomes_mana_regen";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.lifeRegenRegensMana = true;
		this.owner.regen += this.getRegen();
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.lifeRegenRegensMana = false;
		this.owner.regen -= this.getRegen();
	}

	private float getRegen() {
		return this.level * 4;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}
}