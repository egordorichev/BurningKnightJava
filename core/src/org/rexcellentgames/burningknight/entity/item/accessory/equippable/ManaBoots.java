package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaBoots extends Equippable {
	{
		name = Locale.get("mana_boots");
		description = Locale.get("mana_boots_desc");
		sprite = "item-running_boosts_mana_regen";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).flipRegenFormula = true;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).flipRegenFormula = false;
		}
	}
}