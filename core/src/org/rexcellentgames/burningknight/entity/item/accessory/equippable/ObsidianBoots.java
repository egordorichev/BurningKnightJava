package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class ObsidianBoots extends Equippable {
	{
		name = Locale.get("obsidian_boots");
		description = Locale.get("obsidian_boots_desc");
		sprite = "item-obsidian_boots";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.lavaResist += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.lavaResist -= 1;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}