package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.util.Maze;
import org.rexcellentgames.burningknight.util.Random;

public class MazeRoom extends RegularRoom {
	private static byte[] types = new byte[] { Terrain.WALL, Terrain.LAVA, Terrain.WATER };
	private static float[] chanches = new float[] { 3f, 0.3f };

	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();
		
		byte wall = types[Random.chances(chanches)];
		boolean[][] maze = Maze.generate(this);

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, f);

		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				if (maze[x][y] == Maze.FILLED) {
					Painter.set(level, this.left + x, this.top + y, (x == 0 || y == 0
						|| x == this.getWidth() - 1 || y == this.getHeight() - 1) ? Terrain.WALL : wall);
				}
			}
		}
	}

	@Override
	public int getMinWidth() {
		return 14;
	}

	@Override
	public int getMinHeight() {
		return 14;
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