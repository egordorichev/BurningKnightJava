package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.entity.level.rooms.Room;

public class SpecialRoom extends Room {
	public SpecialRoom(Type type) {
		super(type);
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

	public static SpecialRoom create() {
		return new GardenRoom();
	}
}