package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class GoldRing extends Equipable {
	{
		description = Locale.get("gold_ring_desc");
		name = Locale.get("gold_ring");
		sprite = "item (ring B)";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).goldModifier += 0.3f;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).goldModifier -= 0.3f;
		}
	}
}