package org.rexellentgames.dungeon.entity.item.autouse;

import org.rexellentgames.dungeon.entity.creature.player.Player;

public class Backpack extends Autouse {
	@Override
	public void use() {
		super.use();

		this.setCount(this.count - 1);
		Player.instance.ui.resize(18);
	}
}