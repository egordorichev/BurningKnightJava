package org.rexellentgames.dungeon.entity.level.rooms.regular.ladder;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.util.geometry.Point;

public class EntranceRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point point = this.getCenter();

		Entrance entrance = new Entrance();

		entrance.x = point.x * 16;
		entrance.y = point.y * 16 - 8;

		level.addSaveable(entrance);
		Dungeon.area.add(entrance);
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