package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.entity.creature.mob.*;

public class MobPool extends Pool<Mob> {
	public static MobPool instance = new MobPool();

	public void initForFloor() {
		clear();

		add(Knight.class, 0f);
		add(RangedKnight.class, 0f); // should be ~0.5f
		add(StabbingKnight.class, 1f);
		add(Clown.class, 0f);
	}
}