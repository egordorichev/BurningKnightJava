package org.rexellentgames.dungeon.entity.pool;

import org.rexellentgames.dungeon.entity.creature.mob.prefix.*;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class PrefixPool {
	public static PrefixPool instance = new PrefixPool();

	public PrefixPool() {
		add(new MoreHealthPrefix(), 1f);
		add(new MoreDefensePrefix(), 1f);
		add(new ExplosiveDeathPrefix(), 1f);
		add(new HighDodgePrefix(), 1f);
		add(new KnockbackPrefix(), 1f);
		add(new MoreCritChance(), 1f);
		add(new MoreInvTime(), 1f);
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