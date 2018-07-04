package org.rexcellentgames.burningknight.entity.level.rooms.ladder;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Maze;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

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
					} else if (x != 0 && y != 0 && x != getWidth() - 1 && y != getHeight() - 1 && !set && Random.chance(this.getMaxWidth() * this.getHeight() / 100)) {
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