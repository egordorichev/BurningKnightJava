package org.rexellentgames.dungeon.entity.level.rooms;

public class SpecialRoom extends Room {
	public SpecialRoom(Type type) {
		super(type);
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}

	public static SpecialRoom create() {
		return null; // fixme
	}
}