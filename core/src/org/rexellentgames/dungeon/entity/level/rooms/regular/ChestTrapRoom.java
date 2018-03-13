package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class ChestTrapRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		// todo: add a sign with warning
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.WOOD);
		Painter.fill(level, this, 2, Terrain.FLOOR);

		Point center = this.getCenter();
		// todo: chest

		Painter.set(level, center, Terrain.WOOD);
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

	@Override
	public int getMinWidth() {
		return 7;
	}

	@Override
	public int getMaxWidth() {
		return 8;
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMaxHeight() {
		return 8;
	}
}