package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class KillerItem extends Item {
	@Override
	public void use() {
		super.use();
		Player.instance.modifyHp(-Player.instance.getHpMax(),
			null, true);
		Player.instance.setHpMax(0);
		Player.instance.die();
		Player.instance.playSfx("head_explode");
	}
}