package org.rexcellentgames.burningknight.entity.item.active;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;

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