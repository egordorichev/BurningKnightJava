package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaBottle extends Equipable {
	{
		name = Locale.get("mana_bottle");
		description = Locale.get("mana_bottle_desc");
		sprite = "item-mana_bottle";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaRegenRoom = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaRegenRoom = false;
		}
	}
}