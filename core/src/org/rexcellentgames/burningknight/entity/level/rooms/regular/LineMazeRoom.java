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

		for (int x = this.left + 1; x < this.right; x++) {
			if ((x - this.left) % 2 == 0 && !Random.chance(30)) {
				if (Random.chance(30)) {
					Painter.drawLine(level, new Point(x, this.top + 1), new Point(x, this.bottom - 1), Terrain.randomFloor());
					Painter.drawLine(level, new Point(x, this.top + 1), new Point(x, this.bottom - 1), Terrain.LAVA);

					for (int i = 0; i < (Random.chance(20) ? 2 : 1); i++) {
						Painter.set(level, new Point(x, Random.newInt(this.top + 1, this.bottom)), Random.chance(50) ? Terrain.WATER : Terrain.DIRT);
					}
				} else {
					Painter.drawLine(level, new Point(x, this.top + 1), new Point(x, this.bottom - 1), Random.chance(25) ? Terrain.WALL : Terrain.CHASM);

					for (int i = 0; i < (Random.chance(20) ? 2 : 1); i++) {
						Painter.set(level, new Point(x, Random.newInt(this.top + 1, this.bottom)), Terrain.randomFloor());
					}
				}
			} else {
				Painter.drawLine(level, new Point(x, this.top + 1), new Point(x, this.bottom - 1), Terrain.randomFloor());
			}
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (p.x != this.left && p.x != this.right && (p.x - this.left) % 2 == 0) {
			return false;
		}

		return super.canConnect(p);
	}

	@Override
	protected int validateWidth(int w) {
		return w % 2 == 0 ? w : w - 1;
	}
}