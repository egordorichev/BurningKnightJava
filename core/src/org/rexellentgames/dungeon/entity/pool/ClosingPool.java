package org.rexellentgames.dungeon.entity.pool;

import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class ClosingPool<T> extends Pool<T> {
	protected ArrayList<Class<? extends T>> newClasses = new ArrayList<>();
	protected ArrayList<Float> newChances = new ArrayList<>();

	public void reset() {
		newClasses = (ArrayList<Class<? extends T>>) classes.clone();
		newChances = (ArrayList<Float>) chances.clone();
	}

	@Override
	public T generate() {
		if (newChances.size() == 0) {
			reset();
		}

		Class<? extends T> type = newClasses.get(Random.chances(newChances.toArray(new Float[0])));

		newChances.remove(newClasses.indexOf(type));
		newClasses.remove(type);

		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}
}