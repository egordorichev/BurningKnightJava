package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ArcaneBattery extends Equipable {
	{
		name = Locale.get("arcane_battery");
		description = Locale.get("arcane_battery_desc");
		sprite = "item-lower_health_more_mana_regen";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).moreManaRegenWhenLow = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).moreManaRegenWhenLow = false;
		}
	}
}