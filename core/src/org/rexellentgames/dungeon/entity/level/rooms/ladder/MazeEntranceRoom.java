package org.rexellentgames.dungeon.entity.level.rooms.ladder;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Maze;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class MazeEntranceRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		byte wall = Terrain.WALL;
		boolean[][] maze = Maze.generate(this);

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1,  Terrain.randomFloor());

		boolean set = false;

		while (!set) {
			for (int x = 0; x < this.getWidth(); x++) {
				for (int y = 0; y < this.getHeight(); y++) {
					if (maze[x][y] == Maze.FILLED) {
						Painter.set(level, this.left + x, this.top + y, wall);
					} else if (x != 0 && y != 0 && !set && Random.chance(this.getMaxWidth() * this.getHeight() / 100)) {
						set = true;
						place(level, new Point(this.left + x, this.top + y));
					}
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	@Override
	public int getMaxHeight() {
		return 16;
	}

	@Override
	public int getMaxWidth() {
		return 16;
	}
}