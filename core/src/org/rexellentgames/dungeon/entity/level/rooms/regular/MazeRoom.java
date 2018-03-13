package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Maze;
import org.rexellentgames.dungeon.util.Random;

public class MazeRoom extends RegularRoom {
	private static short[] types = new short[] { Terrain.WALL, Terrain.EMPTY, Terrain.SPIKES };

	@Override
	public void paint(Level level) {
		short wall = types[Random.newInt(types.length)];
		boolean[][] maze = Maze.generate(this);

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR);

		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				if (maze[x][y] == Maze.FILLED) {
					Painter.set(level, this.left + x, this.top + y, wall);
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.MAZE);
		}
	}

	@Override
	public int getMinWidth() {
		return 18;
	}

	@Override
	public int getMinHeight() {
		return 18;
	}

	@Override
	public int getMaxHeight() {
		return 19;
	}

	@Override
	public int getMaxWidth() {
		return 19;
	}
}