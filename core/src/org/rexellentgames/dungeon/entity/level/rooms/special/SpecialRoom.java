package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.entity.item.pool.SpecialRoomPool;
import org.rexellentgames.dungeon.entity.level.rooms.Room;

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

	public static void init() {
		SpecialRoomPool.instance.reset();
	}

	public static SpecialRoom create() {
		return SpecialRoomPool.instance.generate();
	}
}