package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class FireBombs extends Equipable {
	{
		name = Locale.get("fire_bombs");
		description = Locale.get("fire_bombs_desc");
		sprite = "item-fire_bomb";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).fireBombs = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).fireBombs = false;
		}
	}
}