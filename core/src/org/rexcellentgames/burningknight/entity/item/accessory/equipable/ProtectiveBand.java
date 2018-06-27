package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ProtectiveBand extends Equipable {
	{
		name = Locale.get("protective_band");
		description = Locale.get("protective_band_desc");
		sprite = "item-black_belt";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).explosionBlock = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).explosionBlock = false;
		}
	}
}