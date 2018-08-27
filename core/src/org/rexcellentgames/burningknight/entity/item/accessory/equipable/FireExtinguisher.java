package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class FireExtinguisher extends Equipable {
	{
		name = Locale.get("fire_extinguisher");
		description = Locale.get("fire_extinguisher_desc");
		sprite = "item-fire_extinguisher";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.fireResist += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.fireResist -= 1;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}