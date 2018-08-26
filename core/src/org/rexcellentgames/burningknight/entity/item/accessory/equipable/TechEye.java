package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class TechEye extends Equipable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		Player.showStats = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		Player.showStats = false;
	}
}