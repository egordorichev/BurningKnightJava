package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class Halo extends Equipable {
	{
		name = Locale.get("halo");
		description = Locale.get("halo_desc");
		sprite = "item-halo";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;
			boolean full = player.getHp() == player.getHpMax();
			player.setHpMax(player.getHpMax() + 4);

			if (full) {
				player.modifyHp(4, null);
			}
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;

			player.modifyMana(3);
			player.damageModifier -= 1;
			player.setHpMax(player.getHpMax() - 4);
		}
	}
}