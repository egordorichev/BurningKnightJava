package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class LuckyCube extends Equipable {
	{
		name = Locale.get("lucky_cube");
		description = Locale.get("lucky_cube_desc");
		sprite = "item-damage_roll";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).luckDamage = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).luckDamage = false;
		}
	}
}