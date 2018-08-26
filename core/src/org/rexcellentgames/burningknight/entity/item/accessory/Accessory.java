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

	private boolean equiped;

	public void onEquip() {
		equiped = true;
		this.triggerEvent("on_equip");
	}

	public void onUnequip() {
		equiped = false;
		this.triggerEvent("on_unequip");
	}

	public boolean isEquiped() {
		return equiped;
	}

	@Override
	protected void registerEvents(LuaTable args) {
		super.registerEvents(args);

		this.registerEvent("on_equip", args);
		this.registerEvent("on_unequip", args);
	}
}