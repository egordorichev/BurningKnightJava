package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.util.Log;

public class ReloadRune extends Equipable {
	{
		sprite = "item-scroll_a";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("reload_speed", 1f);
		Log.info(this.owner.getStat("reload_speed") + " on");
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("reload_speed", -1f);
		Log.info(this.owner.getStat("reload_speed") + " off");
	}
}