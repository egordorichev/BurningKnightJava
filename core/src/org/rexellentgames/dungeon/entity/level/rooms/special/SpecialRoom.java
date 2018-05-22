package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class SpecialRoom extends Room {
	@Override
	public int getMinWidth() {
		return 5;
	}

	public int getMaxWidth() {
		return 10;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	public int getMaxHeight() {
		return 10;
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}

	private static ArrayList<Class<? extends SpecialRoom>> rooms = new ArrayList<>(Arrays.asList(
		TreasureRoom.class, WellRoom.class
	));

	private static ArrayList<Float> chances = new ArrayList<>(Arrays.asList(
		1f, 1f
	));

	private static ArrayList<Class<? extends SpecialRoom>> forFloor;
	private static ArrayList<Float> chancesForFloor;

	public static void init() {
		forFloor = (ArrayList<Class<? extends SpecialRoom>>) rooms.clone();
		chancesForFloor = (ArrayList<Float>) chances.clone();
	}

	public static SpecialRoom create() {
		float[] floatArray = new float[chancesForFloor.size()];
		int i = 0;

		for (Float f : chancesForFloor) {
			floatArray[i++] = (f != null ? f : Float.NaN);
		}

		Class<? extends SpecialRoom> type = forFloor.get(Random.chances(floatArray));

		if (type == null) {
			return null;
		}

		i = forFloor.indexOf(type);

		chancesForFloor.remove(i);
		forFloor.remove(i);

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