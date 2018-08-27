package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class Zoom extends Equippable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		Player.seeMore = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		Player.seeMore = false;
	}
}