package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.*;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.*;
import org.rexcellentgames.burningknight.entity.level.blood.BloodLevel;
import org.rexcellentgames.burningknight.entity.level.levels.creep.CreepLevel;
import org.rexcellentgames.burningknight.entity.level.levels.desert.DesertLevel;
import org.rexcellentgames.burningknight.entity.level.levels.forest.ForestLevel;
import org.rexcellentgames.burningknight.entity.level.levels.hall.HallLevel;
import org.rexcellentgames.burningknight.entity.level.levels.library.LibraryLevel;
import org.rexcellentgames.burningknight.entity.level.levels.tech.TechLevel;

public class MobPool extends Pool<Mob> {
	public static MobPool instance = new MobPool();

	public void initForFloor() {
		clear();

		if (Dungeon.level instanceof HallLevel) {
			/*add(Knight.class, 1f);
			add(RangedKnight.class, 0.5f);
			add(StabbingKnight.class, 0.5f);

			add(Clown.class, 1f);
			add(BurningClown.class, 0.7f);
			add(FreezingClown.class, 0.5f);*/

			add(DashingKnight.class, 1f);
		}

		/*if (Dungeon.level instanceof HallLevel || Dungeon.level instanceof DesertLevel) {
			add(Thief.class, 1f);
			add(InvisThief.class, 0.7f);
			add(BossThief.class, 0.1f);
		}*/

		if (Dungeon.level instanceof DesertLevel) {
			add(Skeleton.class, 1f);
			add(BlackSkeleton.class, 1f);
			add(BrownSkeleton.class, 0.7f);
			add(Mummy.class, 1f);
			add(BrownMummy.class, 0.8f);
			add(GrayMummy.class, 0.7f);
			add(Archeologist.class, 1f);
			add(BlueArcheologist.class, 0.8f);
			add(RedArcheologist.class, 0.5f);
		}

		if (Dungeon.level instanceof LibraryLevel) {
			add(Skeleton.class, 1f);
			// tmp
		}

		if (Dungeon.level instanceof TechLevel) {
			add(Skeleton.class, 1f);
			// tmp
		}

		if (Dungeon.level instanceof CreepLevel) {
			add(Skeleton.class, 1f);
			// tmp
		}

		if (Dungeon.level instanceof ForestLevel) {
			add(Mummy.class, 1f);
			// tmp
		}

		if (Dungeon.level instanceof BloodLevel) {
			add(Knight.class, 1f);
			// tmp
		}
	}
}