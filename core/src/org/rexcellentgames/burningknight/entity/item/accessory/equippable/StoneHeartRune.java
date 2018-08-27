package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class StoneHeartRune extends Equippable {
	{
		name = Locale.get("stone_heart_rune");
		description = Locale.get("stone_heart_rune_desc");
		sprite = "item-scroll_h";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.lowHealthDefense = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.lowHealthDefense = false;
	}
}