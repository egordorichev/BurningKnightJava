package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;

public class Equippable extends Accessory {
	public Player owner = Player.instance;

	@Override
	public void onEquip(boolean load) {
		owner = Player.instance;
		super.onEquip(load);
	}

	public void setOwner(Player owner) {
		super.setOwner(owner);
		this.owner = owner;
	}

	@Override
	public void upgrade() {
		if (equipped) {
			this.onUnequip(false);
		}

		super.upgrade();

		if (equipped) {
			this.onEquip(false);
		}
	}

	// todo: use == quick equip, if you have inventory space
	@Override
	public boolean canBeUsed() {
		return true;
	}
}