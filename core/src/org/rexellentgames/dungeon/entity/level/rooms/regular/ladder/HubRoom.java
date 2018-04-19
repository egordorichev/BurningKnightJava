package org.rexellentgames.dungeon.entity.level.rooms.regular.ladder;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;

public class HubRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);


	}

	@Override
	public int getMinHeight() {
		return 20;
	}

	@Override
	public int getMinWidth() {
		return 20;
	}

	@Override
	public int getMaxHeight() {
		return 21;
	}

	@Override
	public int getMaxWidth() {
		return 21;
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