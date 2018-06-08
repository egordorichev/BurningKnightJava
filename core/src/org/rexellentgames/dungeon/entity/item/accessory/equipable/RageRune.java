package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class RageRune extends Equipable {
	{
		name = Locale.get("rage_rune");
		description = Locale.get("rage_rune_desc");
		sprite = "item (scroll G)";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lowHealthDamage = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).lowHealthDamage = false;
		}
	}
}