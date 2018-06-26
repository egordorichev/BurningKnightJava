package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class MagicShield extends Equipable {
	{
		name = Locale.get("magic_shield");
		description = Locale.get("magic_shield_desc");
		sprite = "item-shield";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("block_chance", 0.1f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("block_chance", -0.1f);
	}
}