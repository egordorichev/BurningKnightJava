package org.rexellentgames.dungeon.entity.level.rooms;

public class ExitRoom extends RegularRoom {
	public ExitRoom() {
		super(Type.EXIT);
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
			return 1;
		}

		return 0;
	}
}