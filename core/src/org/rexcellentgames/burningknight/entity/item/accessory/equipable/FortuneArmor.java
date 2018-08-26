package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.assets.Locale;

public class FortuneArmor extends Equipable {
	{
		name = Locale.get("fortune_armor");
		description = Locale.get("fortune_armor_desc");
		sprite = "item-defense_roll";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).luckDefense = true;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).luckDefense = false;
		}
	}
}