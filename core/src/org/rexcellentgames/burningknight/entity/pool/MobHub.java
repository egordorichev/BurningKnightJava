package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

import java.util.ArrayList;

public class MobHub {
	public ArrayList<Class<? extends Mob>> types;
	public float chance;
	public int maxMatches;
	public boolean once;

	public MobHub(float chance, int max, Class<? extends Mob> ... classes) {
		types = new ArrayList<>();

		for (Class<? extends Mob> c : classes) {
			types.add(c);
		}

		chance = chance;
		max = maxMatches;
	}
}