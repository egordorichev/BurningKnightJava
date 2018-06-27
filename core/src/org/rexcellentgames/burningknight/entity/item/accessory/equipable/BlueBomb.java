package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class BlueBomb extends Equipable {
	{
		name = Locale.get("blue_bomb");
		description = Locale.get("blue_bomb_desc");
		sprite = "item-mana_bombs";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaBombs = true;
			((Player) this.owner).manaRegenRate += 0.2f;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).manaBombs = false;
			((Player) this.owner).manaRegenRate -= 0.2f;
		}
	}
}