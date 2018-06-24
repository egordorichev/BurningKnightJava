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
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).damageModifier += 0.2f;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).damageModifier -= 0.2f;
		}
	}
}