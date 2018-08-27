package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;

public class Equipable extends Accessory {
	public Player owner = Player.instance;
	protected 

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
		this.onUnequip(false);
		super.upgrade();
		this.onEquip(false);
	}
}