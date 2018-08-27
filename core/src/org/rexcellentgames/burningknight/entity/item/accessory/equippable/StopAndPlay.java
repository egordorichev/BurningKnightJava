package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.assets.Locale;

public class StopAndPlay extends Equippable {
	{
		name = Locale.get("stop_and_play");
		description = Locale.get("stop_and_play_desc");
		sprite = "item-pause_and_play";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).pauseMore = true;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);

		if (this.owner instanceof Player) {
			((Player) this.owner).pauseMore = false;
		}
	}
}