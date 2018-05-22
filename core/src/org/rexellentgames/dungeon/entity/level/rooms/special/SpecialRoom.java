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

	private static float[] chances = new float[]{
		1f, 1f
	};

	private static ArrayList<Class<? extends SpecialRoom>> forFloor;

	public static void init() {
		forFloor = (ArrayList<Class<? extends SpecialRoom>>) rooms.clone();
	}

	public static SpecialRoom create() {
		Class<? extends SpecialRoom> type = rooms.get(Random.chances(chances));

		if (type == null) {
			return null;
		}

		forFloor.remove(type);

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