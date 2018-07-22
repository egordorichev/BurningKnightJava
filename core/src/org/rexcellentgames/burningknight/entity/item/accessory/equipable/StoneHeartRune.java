package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class StoneHeartRune extends Equipable {
	{
		name = Locale.get("stone_heart_rune");
		description = Locale.get("stone_heart_rune_desc");
		sprite = "item-scroll_h";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lowHealthDefense = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lowHealthDefense = false;
		}
	}
}