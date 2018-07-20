package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Pool<T> {
	protected ArrayList<Class<? extends T>> classes = new ArrayList<>();
	protected ArrayList<Float> chances = new ArrayList<>();

	public T generate() {
		int i = Random.chances(chances.toArray(new Float[0]));

		if (i == -1) {
			Log.error("-1 as pool result!");
			return null;
		}

		Class<? extends T> type = classes.get(i);

		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void add(Class<? extends T> type, float chance) {
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