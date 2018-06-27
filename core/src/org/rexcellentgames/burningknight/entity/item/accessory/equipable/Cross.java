package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class Cross extends Equipable {
	{
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("inv_time", 0.4f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("inv_time", -0.4f);
	}
}