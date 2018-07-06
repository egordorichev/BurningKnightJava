package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class LineMazeRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int y = this.top + 1; y < this.bottom; y++) {
			if ((y - this.top) % 2 == 0) {
				Painter.drawLine(level, new Point(this.left + 1, y), new Point(this.right - 1, y), Terrain.CHASM);
				Painter.set(level, new Point(Random.newInt(this.left + 1, this.right), y), Terrain.randomFloor());
			} else {
				Painter.drawLine(level, new Point(this.left + 1, y), new Point(this.right - 1, y), Terrain.randomFloor());
			}
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (p.y != this.top && p.y != this.bottom && (p.y - this.top) % 2 == 0) {
			return false;
		}

		return super.canConnect(p);
	}
	@Override
	protected int validateHeight(int h) {
		return h % 2 == 0 ? h : h - 1;
	}
}