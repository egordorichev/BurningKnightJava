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

	public boolean equipped;

	public void onEquip(boolean load) {

	}

	public void onUnequip(boolean load) {

	}
}