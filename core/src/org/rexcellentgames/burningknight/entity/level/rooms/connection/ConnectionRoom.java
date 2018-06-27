package org.rexcellentgames.burningknight.entity.level.rooms.connection;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.pool.room.ConnectionRoomPool;

public class ConnectionRoom extends Room {
	public static ConnectionRoom create() {
		if (Dungeon.depth == -1 || Dungeon.depth == 4) {
			return new TunnelRoom();
		}

		return ConnectionRoomPool.instance.generate();
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