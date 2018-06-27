package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class Zoom extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		Player.seeMore = true;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		Player.seeMore = false;
	}
}