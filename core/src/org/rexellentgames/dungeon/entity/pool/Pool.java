package org.rexellentgames.dungeon.entity.pool;

import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class Pool<T> {
	protected ArrayList<Class<? extends T>> classes = new ArrayList<>();
	protected ArrayList<Float> chances = new ArrayList<>();

	public T generate() {
		Class<? extends T> type = classes.get(Random.chances(chances.toArray(new Float[0])));

		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected void add(Class<? extends T> type, float chance) {
		classes.add(type);
		chances.add(chance);
	}

	public void clear() {
		classes.clear();
		chances.clear();
	}

	public void addFrom(Pool<T> pool) {
		this.classes.addAll(pool.classes);
		this.chances.addAll(pool.chances);
	}
}