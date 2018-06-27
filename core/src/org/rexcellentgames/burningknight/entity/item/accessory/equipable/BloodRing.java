package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class BloodRing extends Equipable {
	{
		description = Locale.get("blood_ring_desc");
		name = Locale.get("blood_ring");
		sprite = "item-ring_c";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).regen += 1f;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).regen -= 1f;
		}
	}
}