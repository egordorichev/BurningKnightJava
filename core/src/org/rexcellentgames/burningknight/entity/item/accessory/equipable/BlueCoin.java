package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class BlueCoin extends Equipable {
	{
		name = Locale.get("blue_coin");
		description = Locale.get("blue_coin_desc");
		sprite = "item-picking_up_coins_gives_mana";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaCoins = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaCoins = false;
		}
	}
}