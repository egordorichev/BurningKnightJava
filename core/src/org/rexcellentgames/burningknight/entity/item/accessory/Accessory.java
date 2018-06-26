package org.rexcellentgames.burningknight.entity.item.accessory;

import org.luaj.vm2.LuaTable;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Accessory extends Item {
	{
		identified = true;
		useable = false;
	}

	public void onEquip() {
		this.triggerEvent("on_equip");
	}

	public void onUnequip() {
		this.triggerEvent("on_unequip");
	}

	@Override
	protected void registerEvents(LuaTable args) {
		super.registerEvents(args);

		this.registerEvent("on_equip", args);
		this.registerEvent("on_unequip", args);
	}
}