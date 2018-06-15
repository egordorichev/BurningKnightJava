package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class PoisonBombs extends Equipable {
	{
		name = Locale.get("poison_bombs");
		description = Locale.get("poison_bombs_desc");
		sprite = "item-poison_bomb";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).poisonBombs = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).poisonBombs = false;
		}
	}
}