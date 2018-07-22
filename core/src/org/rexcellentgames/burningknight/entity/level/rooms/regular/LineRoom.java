package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class LineRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Terrain.randomFloor();
		byte fl = Random.chance(30) ? Terrain.WALL : (Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);
		Painter.fill(level, this, Terrain.WALL);

		if (fl == Terrain.LAVA) {
			f = Random.chance(40) ? Terrain.WATER : Terrain.DIRT;
		}

		Painter.fill(level, this, 1, Terrain.randomFloor());

		int m = 2 + Random.newInt(3);

		Painter.fill(level, this, m, fl);
		Painter.fill(level, this, m + 1, f);

		if (Random.chance(50)) {
			fl = Random.chance(30) ? Terrain.WALL : (Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);

			if (Random.chance(50)) {
				Painter.fill(level, this, m + 2 + Random.newInt(2), fl);
			} else {
				Painter.fillEllipse(level, this, m + 2 + Random.newInt(2), fl);
			}
		}

		Point point;

		switch (Random.newInt(4)) {
			case 0: default: point = new Point(this.getWidth() / 2 + this.left, this.top + m); break;
			case 1: point = new Point(this.getWidth() / 2 + this.left, this.bottom - m); break;
			case 2: point = new Point(this.left + m, this.getHeight() / 2 + this.top); break;
			case 3: point = new Point(this.right - m, this.getHeight() / 2 + this.top); break;
		}

		Painter.set(level, point, f);
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}
}