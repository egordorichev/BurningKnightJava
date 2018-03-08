package org.rexellentgames.dungeon.entity.level.rooms.connection;

import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class ConnectionRoom extends Room {
	private static ArrayList<Class<? extends ConnectionRoom>> rooms = new ArrayList<Class<? extends ConnectionRoom>>(Arrays.asList(
		TunnelRoom.class, RingConnectionRoom.class, ChasmTunnelRoom.class
	));

	private static float[] chances = new float[]{
		5, 3, 2
	};

	public ConnectionRoom(Type type) {
		super(type);
	}

	public static ConnectionRoom create() {
		try {
			return rooms.get(Random.chances(chances)).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return new TunnelRoom();
	}

	@Override
	public int getMinWidth() {
		return 3;
	}

	@Override
	public int getMinHeight() {
		return 3;
	}

	@Override
	public int getMaxWidth() {
		return 10;
	}

	@Override
	public int getMaxHeight() {
		return 10;
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 16;
		}

		return 4;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 2;
		}

		return 0;
	}
}