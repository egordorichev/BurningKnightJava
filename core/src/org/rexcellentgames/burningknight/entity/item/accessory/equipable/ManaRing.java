package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaRing extends Equipable {
	{
		name = Locale.get("mana_ring");
		description = Locale.get("mana_ring_desc");
		sprite = "item-mana_ring";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaRegenRate += 0.5f;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaRegenRate -= 0.5f;
		}
	}
}