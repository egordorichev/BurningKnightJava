package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class RedBook extends Equipable {
	{
		name = Locale.get("red_book");
		description = Locale.get("red_book_desc");
		sprite = "item-life_regen_becomes_mana_regen";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lifeRegenRegensMana = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lifeRegenRegensMana = false;
		}
	}
}