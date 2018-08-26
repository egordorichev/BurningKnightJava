package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class DemageEmblem extends Equipable {
	{
		name = Locale.get("demage_emblem");
		description = Locale.get("demage_emblem_desc");
		sprite = "item-more_defense_and_damage";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.defenseModifier += 0.2f;
		this.owner.damageModifier += 0.2f;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.defenseModifier -= 0.2f;
		this.owner.damageModifier -= 0.2f;
	}
}