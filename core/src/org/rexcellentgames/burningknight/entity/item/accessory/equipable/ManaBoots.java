package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaBoots extends Equipable {
	{
		name = Locale.get("mana_boots");
		description = Locale.get("mana_boots_desc");
		sprite = "item-running_boosts_mana_regen";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).flipRegenFormula = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).flipRegenFormula = false;
		}
	}
}