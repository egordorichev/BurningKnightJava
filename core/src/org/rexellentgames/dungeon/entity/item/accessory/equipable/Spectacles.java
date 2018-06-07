package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class Spectacles extends Equipable {
	{
		name = Locale.get("spectacles");
		description = Locale.get("spectacles_desc");
		sprite = "item-spectacles";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).seeSecrets = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).seeSecrets = false;
		}
	}
}