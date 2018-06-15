package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.entity.creature.mob.*;

public class MobPool extends Pool<Mob> {
	public static MobPool instance = new MobPool();

	public void initForFloor() {
		clear();

		/*add(Knight.class, 1f);
		add(RangedKnight.class, 0.5f);
		add(StabbingKnight.class, 0.5f);*/

		add(Clown.class, 1f);
	}
}