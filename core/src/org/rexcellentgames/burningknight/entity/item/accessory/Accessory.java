package org.rexcellentgames.burningknight.entity.item.accessory;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Accessory extends Item {
	{
		useable = false;
	}

	@Override
	public int getPrice() {
		return 10;
	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	@Override
	public boolean canBeDegraded() {
		return true;
	}

	public boolean equipped;

	public void onEquip(boolean load) {
		this.identify();
	}

	public void onUnequip(boolean load) {

	}
}