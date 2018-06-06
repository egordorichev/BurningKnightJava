package org.rexellentgames.dungeon.entity.item.autouse;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class Backpack extends Autouse {
	{
		description = Locale.get("backpack_desc");
		name = Locale.get("backpack");
		sprite = "item-backpack";
	}

	@Override
	public void use() {
		super.use();

		this.setCount(this.count - 1);
		Player.instance.ui.resize(Math.min(18, Player.instance.ui.getInventory().getSize() + 2));
	}
}