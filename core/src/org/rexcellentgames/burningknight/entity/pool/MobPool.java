package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.DiagonalShotFly;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.common.*;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Archeologist;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Mummy;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Skeleton;
import org.rexcellentgames.burningknight.entity.creature.mob.forest.Hedgehog;
import org.rexcellentgames.burningknight.entity.creature.mob.forest.Treeman;
import org.rexcellentgames.burningknight.entity.creature.mob.forest.Wombat;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Clown;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Knight;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.RangedKnight;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Thief;
import org.rexcellentgames.burningknight.entity.creature.mob.library.Mage;
import org.rexcellentgames.burningknight.entity.level.blood.BloodLevel;
import org.rexcellentgames.burningknight.entity.level.levels.creep.CreepLevel;
import org.rexcellentgames.burningknight.entity.level.levels.desert.DesertLevel;
import org.rexcellentgames.burningknight.entity.level.levels.forest.ForestLevel;
import org.rexcellentgames.burningknight.entity.level.levels.hall.HallLevel;
import org.rexcellentgames.burningknight.entity.level.levels.library.LibraryLevel;
import org.rexcellentgames.burningknight.entity.level.levels.tech.TechLevel;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class MobPool {
	public static MobPool instance = new MobPool();
	protected ArrayList<MobHub> classes = new ArrayList<>();
	protected ArrayList<Float> chances = new ArrayList<>();

	protected ArrayList<MobHub> dclasses = new ArrayList<>();
	protected ArrayList<Float> dchances = new ArrayList<>();

	public void initForRoom() {
		for (int i = 0; i < dchances.size(); i++) {
			classes.add(dclasses.get(i));
			chances.add(dchances.get(i));
		}

		dchances.clear();
		dclasses.clear();
	}

	public MobHub generate() {
		int i = Random.chances(chances.toArray(new Float[0]));

		if (i == -1) {
			Log.error("-1 as pool result!");
			return null;
		}

		MobHub hub = classes.get(i);

		if (hub != null) {
			hub.maxMatches -= 1;

			if (hub.maxMatches == 0) {
				if (!hub.once) {
					dchances.add(chances.get(classes.indexOf(hub)));
					dclasses.add(hub);
				}

				chances.remove(classes.indexOf(hub));
				classes.remove(hub);
			}
		}

		return hub;
	}

	public void add(MobHub type, float chance) {
		classes.add(type);
		chances.add(chance);
	}

	public void clear() {
		classes.clear();
		chances.clear();
	}

	private void add(float chance, int max, Class<? extends Mob> ... classes) {
		add(new MobHub(chance, max, classes), chance);
	}

	public void initForFloor() {
		clear();

		add(0.5f, -1, MovingFly.class);
		add(1f, 1, DiagonalFly.class);
		add(0.3f, 1, BurningMan.class);
		add(0.1f, 1, SupplyMan.class);
		add(0.1f, 1, CoinMan.class);
		add(0.1f, 1, BombMan.class);

		if (Dungeon.depth > 1) {
			add(0.5f, -1, MovingFly.class, MovingFly.class, MovingFly.class, MovingFly.class);
			add(1f, -1, DiagonalShotFly.class);
		}

		if (Dungeon.level instanceof HallLevel) {
			add(1f, -1, RangedKnight.class);
			add(1f, -1, Knight.class);
			add(1f, -1, Clown.class);

			if (Dungeon.depth > 1) {
				add(1f, -1, Thief.class);
			}
		}

		if (Dungeon.level instanceof DesertLevel) {
			add(1f, -1, Archeologist.class);
			add(1f, -1, Mummy.class);
			add(1f, -1, Thief.class);

			if (Dungeon.depth > 3) {
				add(0.5f, 1, Skeleton.class);
			}
		}

		if (Dungeon.level instanceof ForestLevel) {
			add(1f, 1, Wombat.class);
			add(1f, -1, Hedgehog.class);
			add(1f, 2, Treeman.class);
		}

		if (Dungeon.level instanceof LibraryLevel) {
			add(1000000000000000f, -1, Mage.class);
		}

		// TMP
		if (Dungeon.level instanceof TechLevel ||
			Dungeon.level instanceof CreepLevel ||
			Dungeon.level instanceof BloodLevel) {

			add(1f, -1, Knight.class);
		}
	}
}