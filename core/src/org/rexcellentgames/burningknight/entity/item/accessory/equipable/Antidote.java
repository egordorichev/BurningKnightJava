package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class Antidote extends Equipable {
	{
		name = Locale.get("antidote");
		description = Locale.get("antidote_desc");
		sprite = "item-antidote";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).poisonResist += 1;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).poisonResist -= 1;
		}
	}
}