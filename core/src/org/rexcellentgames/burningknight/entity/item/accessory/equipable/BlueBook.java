package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class BlueBook extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.modifyManaMax(-3);
			player.damageModifier += 1;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.modifyMana(+3);
			player.damageModifier -= 1;
		}
	}
}