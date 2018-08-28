package org.rexcellentgames.burningknight.entity.level.rooms.treasure;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Maze;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class MazeTreasureRoom extends TreasureRoom {
	private static byte[] types = new byte[]{Terrain.WALL, Terrain.CHASM};
	private static float[] chanches = new float[]{1, 1f};

	@Override
	public void paint(Level level) {
		super.paint(level);

		byte wall = types[Random.chances(chanches)];
		boolean[][] maze = Maze.generate(this);

		Painter.fill(level, this, Terrain.WALL);

		if (Random.chance(50)) {
			Painter.fill(level, this, 1, Terrain.FLOOR_D);
		} else {
			Painter.fill(level, this, 1, Terrain.randomFloor());
			Painter.fillEllipse(level, this, 1, Terrain.FLOOR_D);
		}

		if (Random.chance(50)) {

			if (Random.chance(50)) {
				Painter.fill(level, this, Random.newInt(2, 5), Terrain.randomFloor());
			} else {
				Painter.fillEllipse(level, this, Random.newInt(2, 5), Terrain.randomFloor());
			}
		}

		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				if (maze[x][y] == Maze.FILLED) {
					Painter.set(level, this.left + x, this.top + y, (x == 0 || y == 0
						|| x == this.getWidth() - 1 || y == this.getHeight() - 1) ? Terrain.WALL : wall);
				}
			}
		}

		Point center = this.getCenter();

		if (Random.chance(50)) {
			Painter.fill(level, (int) center.x - 1, (int) center.y - 1, 3, 3, Random.chance(50) ? Terrain.FLOOR_B : Terrain.FLOOR_D);
		} else {
			Painter.fillEllipse(level, (int) center.x - 1, (int) center.y - 1, 3, 3, Random.chance(50) ? Terrain.FLOOR_B : Terrain.FLOOR_D);
		}

		placeChest(center);
	}

	@Override
	public int getMinWidth() {
		return 13;
	}

	@Override
	public int getMinHeight() {
		return 13;
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