package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Maze;
import org.rexellentgames.dungeon.util.Random;

public class MazeRoom extends RegularRoom {
	private static byte[] types = new byte[] { Terrain.WALL, Terrain.LAVA, Terrain.WATER };
	private static float[] chanches = new float[] { 3f, 0.3f };

	@Override
	public void paint(Level level) {
		byte wall = types[Random.chances(chanches)];
		boolean[][] maze = Maze.generate(this);

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR);

		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				if (maze[x][y] == Maze.FILLED) {
					Painter.set(level, this.left + x, this.top + y, (x == 0 || y == 0
						|| x == this.getWidth() - 1 || y == this.getHeight() - 1) ? Terrain.WALL : wall);
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