package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class StarOnAString extends Equipable {
	{
		name = Locale.get("star_on_a_string");
		description = Locale.get("star_on_a_string_desc");
		sprite = "item-mana_up";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).modifyManaMax(2);
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).modifyManaMax(-2);
		}
	}
}