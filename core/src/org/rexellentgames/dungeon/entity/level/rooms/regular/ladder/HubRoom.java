package org.rexellentgames.dungeon.entity.level.rooms.regular.ladder;

import org.rexellentgames.dungeon.entity.level.Level;

public class HubRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		// todo

		level.entrance = this;
	}

	@Override
	public int getMinHeight() {
		return 12;
	}

	@Override
	public int getMinWidth() {
		return 12;
	}

	@Override
	public int getMaxHeight() {
		return 13;
	}

	@Override
	public int getMaxWidth() {
		return 13;
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 16;
		}

		if (side == Connection.BOTTOM) {
			return 0;
		}

		return 4;
	}
}