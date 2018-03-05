package org.rexellentgames.dungeon.entity.level.rooms;

public class EntranceRoom extends RegularRoom {
	public EntranceRoom() {
		super(Type.ENTRANCE);
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

	@Override
	public int getMinWidth() {
		return Math.max(super.getMinWidth(), 5);
	}

	@Override
	public int getMinHeight() {
		return Math.max(super.getMinHeight(), 5);
	}
}