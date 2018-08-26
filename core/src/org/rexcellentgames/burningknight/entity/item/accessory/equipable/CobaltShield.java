package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class CobaltShield extends Equipable {
	{
		name = Locale.get("cobalt_shield");
		description = Locale.get("cobalt_shield_desc");
		sprite = "item-obsidian_shield";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("knockback", -1);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("knockback", 1);
	}
}