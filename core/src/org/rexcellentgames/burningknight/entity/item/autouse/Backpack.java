package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class Backpack extends Autouse {
	{
		description = Locale.get("backpack_desc");
		name = Locale.get("backpack");
		sprite = "item-backpack";
	}

	@Override
	public void use() {
		super.use();
		Player.instance.ui.resize(Math.min(6 * 5, Player.instance.ui.getInventory().getSize() + 6));
	}
}