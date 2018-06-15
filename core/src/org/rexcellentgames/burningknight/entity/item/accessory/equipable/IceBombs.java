package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class IceBombs extends Equipable {
	{
		name = Locale.get("ice_bombs");
		description = Locale.get("ice_bombs_desc");
		sprite = "item-ice_bomb";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).iceBombs = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).iceBombs = false;
		}
	}
}