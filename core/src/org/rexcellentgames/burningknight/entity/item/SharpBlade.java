package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class SharpBlade extends Item {
	@Override
	public void use() {
		if (delay > 0) {
			return;
		}

		super.use();
		Player.instance.modifyHp(-1, null);
	}
}