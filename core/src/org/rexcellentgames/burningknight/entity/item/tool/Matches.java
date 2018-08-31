package org.rexcellentgames.burningknight.entity.item.tool;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.level.Level;

public class Matches extends Tool {
	{
		useTime = 10f;
	}

	@Override
	public void use() {
		super.use();
		this.owner.addBuff(new BurningBuff());
		int i = Level.toIndex(Math.round((this.owner.x) / 16), Math.round((this.owner.y + this.owner.h / 2) / 16));

		Dungeon.level.setOnFire(i, true);
	}
}