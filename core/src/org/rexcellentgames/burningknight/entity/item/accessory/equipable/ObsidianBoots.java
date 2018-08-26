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
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).lavaResist += 1;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).lavaResist -= 1;
		}
	}
}