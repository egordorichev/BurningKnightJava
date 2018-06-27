package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class Backpack extends Autouse {
	{
	}

	@Override
	public void use() {
		super.use();

		this.setCount(this.count - 1);
		Player.instance.ui.resize(Math.min(18, Player.instance.ui.getInventory().getSize() + 2));
	}
}