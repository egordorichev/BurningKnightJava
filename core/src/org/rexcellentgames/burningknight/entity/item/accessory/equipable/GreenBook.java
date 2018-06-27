package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class GreenBook extends Equipable {
	{
		name = Locale.get("green_book");
		description = Locale.get("green_book_desc");
		sprite = "item-less_mana_more_damage";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.modifyManaMax(-4);
			player.damageModifier += 1;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;


			player.modifyManaMax(+4);
			player.damageModifier -= 1;
		}
	}
}