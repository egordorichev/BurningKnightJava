package org.rexellentgames.dungeon.entity.level.rooms.ladder;

import org.rexellentgames.dungeon.entity.level.rooms.Room;

public class LadderRoom extends Room {
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

	@Override
	public int getMinWidth() {
		return 5;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	@Override
	public int getMaxWidth() {
		return 13;
	}

	@Override
	public int getMaxHeight() {
		return 13;
	}
}