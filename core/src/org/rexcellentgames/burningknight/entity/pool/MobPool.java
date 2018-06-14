package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.entity.creature.mob.Clown;
import org.rexcellentgames.burningknight.entity.creature.mob.Knight;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.RangedKnight;

public class MobPool extends Pool<Mob> {
	public static MobPool instance = new MobPool();

	public void initForFloor() {
		clear();

		add(Knight.class, 1f);
		add(RangedKnight.class, 0.7f);
		add(Clown.class, 1.2f);
	}
}