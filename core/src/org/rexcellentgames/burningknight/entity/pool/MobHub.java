package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

import java.util.ArrayList;
import java.util.Arrays;

public class MobHub {
	public ArrayList<Class<? extends Mob>> types;
	public float chance;
	public int maxMatches;
	public int maxMatchesInitial;
	public boolean once;

	public MobHub(float chance, int max, Class<? extends Mob> ... classes) {
		types = new ArrayList<>();
		types.addAll(Arrays.asList(classes));

		this.chance = chance;
		maxMatches = max;
		maxMatchesInitial = max;
	}
}