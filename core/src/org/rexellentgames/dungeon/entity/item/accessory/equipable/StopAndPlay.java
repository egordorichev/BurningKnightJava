package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class StopAndPlay extends Equipable {
	{
		name = Locale.get("stop_and_play");
		description = Locale.get("stop_and_play_desc");
		sprite = "item-pause_and_play";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		if (this.owner instanceof Player) {
			((Player) this.owner).pauseMore = true;
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		if (this.owner instanceof Player) {
			((Player) this.owner).pauseMore = false;
		}
	}
}