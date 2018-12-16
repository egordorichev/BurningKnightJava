package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.DiagonalShotFly;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.common.DiagonalFly;
import org.rexcellentgames.burningknight.entity.creature.mob.common.Fly;
import org.rexcellentgames.burningknight.entity.creature.mob.common.MovingFly;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Archeologist;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Mummy;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Skeleton;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Clown;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Knight;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.RangedKnight;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Thief;
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
		add(Fly.class, 0.2f);
		add(MovingFly.class, 0.5f);
		add(DiagonalFly.class, 1f);
		add(DiagonalShotFly.class, 0.5f);

		if (Dungeon.level instanceof HallLevel) {
			add(RangedKnight.class, 1f);
			add(Knight.class, 1f);
			add(Clown.class, 1f);
			add(Thief.class, 1f);
		}

		if (Dungeon.level instanceof DesertLevel) {
			add(Archeologist.class, 1f);
			add(Mummy.class, 1f);
			add(Skeleton.class, 0.5f);
			add(Thief.class, 1f);
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