package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class CampfireInABottle extends Equipable {
	{
		name = Locale.get("campfire_in_a_bottle");
		description = Locale.get("campfire_in_a_bottle_desc");
		sprite = "item-campfire_in_a_bottle";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).stunResist += 1;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).stunResist -= 1;
		}
	}
}