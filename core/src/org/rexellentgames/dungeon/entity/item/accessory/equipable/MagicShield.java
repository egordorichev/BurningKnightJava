package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class MagicShield extends Equipable {
	{
		name = Locale.get("magic_shield");
		description = Locale.get("magic_shield_desc");
		sprite = "item-shield";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).blockChance += 10;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).blockChance -= 10;
		}
	}
}