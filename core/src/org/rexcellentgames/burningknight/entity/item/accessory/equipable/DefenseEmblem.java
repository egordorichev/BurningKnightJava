package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class DefenseEmblem extends Equipable {
	{
		name = Locale.get("defense_emblem");
		description = Locale.get("defense_emblem_desc");
		sprite = "item-more_defense";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).defenseModifier += 0.4f;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).defenseModifier -= 0.4f;
		}
	}
}