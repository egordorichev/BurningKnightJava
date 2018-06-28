package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.util.Log;

public class ReloadRune extends Equipable {
	{
		sprite = "item-scroll_a";
	}

	// broken
	@Override
	public void onEquip() {
		super.onEquip();
		Log.info(this.owner.getStat("reload_time") + "");
		this.owner.modifyStat("reload_time", -0.5f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("reload_time", 0.5f);
	}
}