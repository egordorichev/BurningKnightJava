package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

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