package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class StoneHeartRune extends Equipable {
	{
		name = Locale.get("stone_heart_rune");
		description = Locale.get("stone_heart_rune_desc");
		sprite = "item (scroll H)";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lowHealthDefense = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lowHealthDefense = false;
		}
	}
}