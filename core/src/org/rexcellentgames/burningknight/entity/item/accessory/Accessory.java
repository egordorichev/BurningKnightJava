package org.rexcellentgames.burningknight.entity.item.accessory;

import org.luaj.vm2.LuaTable;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Accessory extends Item {
	{
		identified = true;
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
		this.triggerEvent("on_equip");
	}

	public void onUnequip(boolean load) {
		this.triggerEvent("on_unequip");
	}

	@Override
	protected void registerEvents(LuaTable args) {
		super.registerEvents(args);

		this.registerEvent("on_equip", args);
		this.registerEvent("on_unequip", args);
	}
}