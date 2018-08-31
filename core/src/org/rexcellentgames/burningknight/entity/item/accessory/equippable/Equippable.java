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

	@Override
	public boolean isUseable() {
		return true;
	}

	@Override
	public boolean canBeUsed() {
		for (int i = 7; i < 11; i++) {
			if (Player.instance.getInventory().getSlot(i) == null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void use() {
		super.use();

		for (int i = 0; i < Player.instance.getInventory().getSize(); i++) {
			if (Player.instance.getInventory().getSlot(i) == this) {
				Player.instance.getInventory().setSlot(i, null);
				break;
			}
		}

		for (int i = 7; i < 11; i++) {
			if (Player.instance.getInventory().getSlot(i) == null) {
				Player.instance.getInventory().setSlot(i, this);
				this.onEquip(false);
				return;
			}
		}
	}
}