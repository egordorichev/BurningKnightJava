package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class CobaltShield extends Equipable {
	{
		name = Locale.get("cobalt_shield");
		description = Locale.get("cobalt_shield_desc");
		sprite = "item-obsidian_shield";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).knockbackMod = 0;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).knockbackMod = 1;
		}
	}
}