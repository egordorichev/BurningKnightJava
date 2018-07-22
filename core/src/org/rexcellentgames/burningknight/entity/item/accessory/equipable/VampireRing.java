package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class VampireRing extends Equipable {
	{
		description = Locale.get("vampire_ring_desc");
		name = Locale.get("vampire_ring");
		sprite = "item-ring_j";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).vampire += 5f;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).vampire -= 5f;
		}
	}
}