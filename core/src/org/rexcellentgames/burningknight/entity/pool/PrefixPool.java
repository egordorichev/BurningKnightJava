package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.entity.creature.mob.prefix.*;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class PrefixPool {
	public static PrefixPool instance = new PrefixPool();

	public PrefixPool() {
		add(new DeathShotPrefix(), 1f);

		// ideas: greed
		// unkillable till everyone else is killed
	}

	public Prefix getModifier(int id) {
		return classes.get(id);
	}

	protected ArrayList<Prefix> classes = new ArrayList<>();
	protected ArrayList<Float> chances = new ArrayList<>();

	protected void add(Prefix type, float chance) {
		type.id = this.classes.size();

		classes.add(type);
		chances.add(chance);
	}

	public Prefix generate() {
		return classes.get(Random.chances(chances.toArray(new Float[0])));
	}
}