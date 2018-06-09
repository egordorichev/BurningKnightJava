package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Maze;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class SmallMazeRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Terrain.randomFloor();
		Painter.fill(level, this, 1, f);

		Painter.drawLine(level, new Point(this.left + 4, this.top + 1), new Point(this.left + 4, this.bottom - 1), Terrain.WALL);
		Painter.drawLine(level, new Point(this.right - 4, this.top + 1), new Point(this.right - 4, this.bottom - 1), Terrain.WALL);

		Painter.drawLine(level, new Point(this.left + 1, this.top + 4), new Point(this.right - 1, this.top + 4), Terrain.WALL);
		Painter.drawLine(level, new Point(this.left + 1, this.bottom - 4), new Point(this.right - 1, this.bottom - 4), Terrain.WALL);

		if (Random.chance(30)) {
			boolean[][] maze = Maze.generate(this);

			for (int x = 1; x < getWidth() - 1; x++) {
				for (int y = 1; y < getHeight() - 1; y++) {
					if (maze[x][y] == Maze.EMPTY) {
						Painter.set(level, new Point(this.left + x, this.top + y), f);
					}
				}
			}
		} else {
			if (Random.chance(50)) {
				Painter.drawLine(level, new Point(this.left + 1, this.top + 2), new Point(this.right - 1, this.top + 2), f);
				Painter.drawLine(level, new Point(this.left + 1, this.top + this.getHeight() / 2),
					new Point(this.right - 1, this.top + this.getHeight() / 2), f);
				Painter.drawLine(level, new Point(this.left + 1, this.bottom - 2), new Point(this.right - 1, this.bottom - 2), f);

				switch (Random.newInt(3)) {
					case 0:
						Painter.drawLine(level, new Point(this.right - 2, this.top + 1), new Point(this.right - 2, this.bottom - 1), f);
						break;
					case 1:
						Painter.drawLine(level, new Point(this.left + this.getWidth() / 2, this.top + 1),
							new Point(this.left + this.getWidth() / 2, this.bottom - 1), f);
						break;
					case 2:
						Painter.drawLine(level, new Point(this.left + 2, this.top + 1), new Point(this.left + 2, this.bottom - 1), f);
						break;
				}
			} else {
				Painter.drawLine(level, new Point(this.left + 2, this.top + 1), new Point(this.left + 2, this.bottom - 1), f);
				Painter.drawLine(level, new Point(this.left + this.getWidth() / 2, this.top + 1),
					new Point(this.left + this.getWidth() / 2, this.bottom - 1), f);
				Painter.drawLine(level, new Point(this.right - 2, this.top + 1), new Point(this.right - 2, this.bottom - 1), f);

				switch (Random.newInt(3)) {
					case 0:
						Painter.drawLine(level, new Point(this.left + 1, this.top + 2), new Point(this.right - 1, this.top + 2), f);
						break;
					case 1:
						Painter.drawLine(level, new Point(this.left + 1, this.top + this.getHeight() / 2),
							new Point(this.right - 1, this.top + this.getHeight() / 2), f);
						break;
					case 2:
						Painter.drawLine(level, new Point(this.left + 1, this.bottom - 2), new Point(this.right - 1, this.bottom - 2), f);
						break;
				}
			}
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (!(p.x == this.left + 2 && p.y == this.top)
			&& !(p.x == this.left && p.y == this.top + 2)
			&& !(p.x == this.right - 2 && p.y == this.top)
			&& !(p.x == this.right && p.y == this.top + 2)
			&& !(p.x == this.left + 2 && p.y == this.bottom)
			&& !(p.x == this.left && p.y == this.bottom - 2)
			&& !(p.x == this.right - 2 && p.y == this.bottom)
			&& !(p.x == this.right && p.y == this.bottom - 2)) {

			return false;
		}

		return super.canConnect(p);
	}

	@Override
	public int getMinWidth() {
		return 13;
	}

	@Override
	public int getMinHeight() {
		return 13;
	}
}