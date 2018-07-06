package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.entity.pool.room.SpecialRoomPool;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;

public class SpecialRoom extends Room {
	@Override
	public int getMinWidth() {
		return 8;
	}

	public int getMaxWidth() {
		return 14;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	public int getMaxHeight() {
		return 14;
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

	public static void init() {
		SpecialRoomPool.instance.reset();
	}

	public static SpecialRoom create() {
		return SpecialRoomPool.instance.generate();
	}
}