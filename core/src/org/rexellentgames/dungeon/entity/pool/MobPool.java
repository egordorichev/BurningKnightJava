package org.rexellentgames.dungeon.entity.pool;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.Clown;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.mob.RangedKnight;

public class MobPool extends Pool<Mob> {
	public static MobPool instance = new MobPool();

	public void initForFloor() {
		clear();

		if (Dungeon.depth > 0) {
			add(Knight.class, 1f);
		}

		if (Dungeon.depth > 1) {
			add(RangedKnight.class, 0.7f);
		}

		if (Dungeon.depth > 2) {
			add(Clown.class, 1.2f);
		}
	}
}