package org.rexellentgames.dungeon.entity.level.rooms.regular.ladder;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.util.geometry.Point;

public class ExitRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point point = this.getCenter();
		Painter.set(level, (int) point.x, (int) point.y, Terrain.FLOOR_B);

		Exit exit = new Exit();

		exit.x = point.x * 16;
		exit.y = point.y * 16 - 8;

		level.set((int) point.x, (int) point.y, Terrain.EXIT);

		level.addSaveable(exit);
		Dungeon.area.add(exit);

		for (Door door : this.connected.values()) {
			door.setType(Dungeon.depth <= 1 ? Door.Type.ENEMY : Door.Type.LEVEL_LOCKED);
		}
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