package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class YellowBook extends Equipable {
	{
		name = Locale.get("yellow_book");
		description = Locale.get("yellow_book_desc");
		sprite = "item-more_mana_less_damage";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.modifyManaMax(6);
			player.damageModifier -= 0.5;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.modifyManaMax(-6);
			player.damageModifier += 0.5;
		}
	}
}