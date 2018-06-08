package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class DewVial extends Equipable {
	{
		name = Locale.get("dew_vial");
		description = Locale.get("dew_vial_desc");
		sprite = "item-dew_vial";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).healOnEnter = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).healOnEnter = false;
		}
	}
}