package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class CampfireInABottle extends Equipable {
	{
		name = Locale.get("campfire_in_a_bottle");
		description = Locale.get("campfire_in_a_bottle_desc");
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).stunResist = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).stunResist = false;
		}
	}
}