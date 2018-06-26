package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class FortuneRing extends Equipable {
	{
		description = Locale.get("fortune_ring_desc");
		name = Locale.get("fortune_ring");
		sprite = "item-ring_i";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("crit_chance", 0.1f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("crit_chance", -0.1f);
	}
}