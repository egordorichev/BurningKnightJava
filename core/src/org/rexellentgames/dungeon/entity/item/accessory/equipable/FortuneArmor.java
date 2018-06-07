package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class FortuneArmor extends Equipable {
	{
		name = Locale.get("fortune_armor");
		description = Locale.get("fortune_armor_desc");
		sprite = "item-defense_roll";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).luckDefense = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).luckDefense = false;
		}
	}
}