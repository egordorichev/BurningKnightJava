package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class ClosingPool<T> extends Pool<T> {
	protected ArrayList<Class<? extends T>> newClasses = new ArrayList<>();
	protected ArrayList<Float> newChances = new ArrayList<>();

	@SuppressWarnings("unchecked")
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
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}
}