package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Maze;

public class MazeFloorRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		boolean[][] maze = Maze.generate(this);

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR_B);

		for (int x = 1; x < this.getWidth() - 1; x++) {
			for (int y = 1; y < this.getHeight() - 1; y++) {
				if (maze[x][y] == Maze.FILLED) {
					Painter.set(level, this.left + x, this.top + y, Terrain.DIRT);
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}