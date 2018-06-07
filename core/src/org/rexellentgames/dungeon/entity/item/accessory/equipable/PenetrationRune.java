package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;

public class PenetrationRune extends Equipable {
	{
		name = Locale.get("penetration_rune");
		description = Locale.get("penetration_rune_desc");
		sprite = "item-penetration_stone";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.penetrates = true;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.penetrates = false;
	}
}