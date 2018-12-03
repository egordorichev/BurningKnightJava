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
		// add(Thief.class, 1f);

		if (Dungeon.level instanceof HallLevel) {
			add(RangedKnight.class, 1f);
			add(Knight.class, 1f);

			if (Dungeon.depth > 1) {
				add(Clown.class, 1f);
				add(Thief.class, 1f);
			}

			// add(DashingKnight.class, 1f);
		}

		if (Dungeon.level instanceof DesertLevel) {
			add(Mummy.class, 1f);
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