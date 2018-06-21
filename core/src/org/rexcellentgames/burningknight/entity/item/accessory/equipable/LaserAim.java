package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class LaserAim extends Equipable {
	{
		name = Locale.get("laser_aim");
		description = Locale.get("laser_aim");
		sprite = "item-laser_pointer";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).hasRedLine = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).hasRedLine = false;
		}
	}
}