package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class BlueBook extends Equipable {
	{
		name = Locale.get("blue_book");
		description = Locale.get("blue_book_desc");
		sprite = "item-less_mana_used_but_minus_hp_percent";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.setHpMax(player.getHpMax() - 4);
			player.manaModifier -= 0.5;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;


			player.setHpMax(player.getHpMax() + 4);
			player.manaModifier += 0.5;
		}
	}
}