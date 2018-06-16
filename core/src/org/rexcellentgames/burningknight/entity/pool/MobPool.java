package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.BlackSkeleton;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Skeleton;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.*;
import org.rexcellentgames.burningknight.entity.level.levels.desert.DesertLevel;
import org.rexcellentgames.burningknight.entity.level.levels.hall.HallLevel;
import org.rexcellentgames.burningknight.entity.level.levels.library.LibraryLevel;

public class MobPool extends Pool<Mob> {
	public static MobPool instance = new MobPool();

	public void initForFloor() {
		clear();

		if (Dungeon.level instanceof HallLevel) {
			add(Knight.class, 1f);
			add(RangedKnight.class, 0.5f);
			add(StabbingKnight.class, 0.5f);

			add(Clown.class, 1f);
			add(BurningClown.class, 0.7f);
			add(FreezingClown.class, 0.5f);
		}

		if (Dungeon.level instanceof HallLevel || Dungeon.level instanceof DesertLevel) {
			add(Thief.class, 1f);
			add(InvisThief.class, 0.7f);
			add(BossThief.class, 0.1f);
		}

		if (Dungeon.level instanceof DesertLevel) {
			add(Skeleton.class, 1f);
			add(BlackSkeleton.class, 1f);
		}

		if (Dungeon.level instanceof LibraryLevel) {
			add(Skeleton.class, 1f);
			// tmp
		}
	}
}