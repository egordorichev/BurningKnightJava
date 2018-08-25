package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

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
			((Player) this.owner).lavaResist += 1;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lavaResist -= 1;
		}
	}
}