package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class DamageEmblem extends Equipable {
	{
		name = Locale.get("damage_emblem");
		description = Locale.get("damage_emblem_desc");
		sprite = "item-more_damage";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).damageModifier += 0.4f;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).damageModifier -= 0.4f;
		}
	}
}