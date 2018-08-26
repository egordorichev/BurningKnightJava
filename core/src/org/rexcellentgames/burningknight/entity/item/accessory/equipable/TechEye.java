package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class TechEye extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		Player.showStats = true;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Player.showStats = false;
	}
}