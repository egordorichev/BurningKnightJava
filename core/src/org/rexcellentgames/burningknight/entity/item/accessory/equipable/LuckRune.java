package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;

public class LuckRune extends Equipable {
	{
		name = Locale.get("luck_rune");
		description = Locale.get("luck_rune_desc");
		sprite = "item (scroll F)";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		WeaponBase.luck = true;

		for (int i = 0; i < Player.instance.getInventory().getSize(); i++) {
			Item item = Player.instance.getInventory().getSlot(i);

			if (item instanceof WeaponBase) {
				((WeaponBase) item).generateModifier();
			}
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		WeaponBase.luck = false;
	}
}