package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;

public class FortuneRing extends Equipable {
	{
		description = Locale.get("fortune_ring_desc");
		name = Locale.get("fortune_ring");
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.critChance += 10f;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.critChance -= 10f;
	}
}