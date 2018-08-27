package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class Wings extends Equippable {
	{
		name = Locale.get("wings");
		description = Locale.get("wings_desc");
		sprite = "item-wings";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.flight += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.flight -= 1;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}