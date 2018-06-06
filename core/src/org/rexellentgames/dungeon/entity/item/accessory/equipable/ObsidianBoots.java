package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class ObsidianBoots extends Equipable {
	{
		name = Locale.get("obsidian_boots");
		description = Locale.get("obsidian_boots_desc");
		sprite = "item-obsidian_boots";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lavaResist = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lavaResist = false;
		}
	}
}