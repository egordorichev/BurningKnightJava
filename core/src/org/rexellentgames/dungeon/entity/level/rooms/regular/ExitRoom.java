package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class ExitRoom extends RegularRoom {
	public ExitRoom() {
		super(Type.EXIT);
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		Point point = this.getCenter();
		Painter.set(level, (int) point.x, (int) point.y, Terrain.EXIT);
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
		return Math.max(super.getMinWidth(), 8);
	}

	@Override
	public int getMinHeight() {
		return Math.max(super.getMinHeight(), 8);
	}
}