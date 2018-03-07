package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class EntranceRoom extends RegularRoom {
	public EntranceRoom() {
		super(Type.ENTRANCE);
	}

	public EntranceRoom(Type type) {
		super(type);
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		Point point = this.getCenter();
		// Painter.set(level, (int) point.x, (int) point.y, Terrain.ENTRANCE);

		Entrance entrance = new Entrance();

		entrance.x = point.x * 16;
		entrance.y = point.y * 16;

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