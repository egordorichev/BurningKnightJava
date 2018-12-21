package org.rexcellentgames.burningknight.entity.item.active;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;

public class DullBlade extends Item {
	@Override
	public void use() {
		if (delay > 0) {
			return;
		}

		super.use();
		Player.instance.dullDamage = true;
		Player.instance.setInvt(Player.instance.getStat("inv_time"));
		Player.instance.onHurt(-1, null);
	}
}