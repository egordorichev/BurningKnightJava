package org.rexellentgames.dungeon.entity.level.rooms.connection;

import org.rexellentgames.dungeon.entity.level.rooms.Room;

public class ConnectionRoom extends Room {
	public ConnectionRoom(Type type) {
		super(type);
	}

	public static ConnectionRoom create() {
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