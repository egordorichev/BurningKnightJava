package org.rexcellentgames.burningknight.entity.pool;

import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class ClosingPool<T> extends Pool<T> {
	protected ArrayList<Class> newClasses = new ArrayList<>();
	protected ArrayList<Float> newChances = new ArrayList<>();

	public void reset() {
		newClasses = (ArrayList<Class>) classes.clone();
		newChances = (ArrayList<Float>) chances.clone();
	}

	@Override
	public T generate() {
		if (newChances.size() == 0) {
			reset();
		}

		Class type = newClasses.get(Random.chances(newChances.toArray(new Float[0])));

		newChances.remove(newClasses.indexOf(type));
		newClasses.remove(type);

		try {
			return (T) type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}
}