package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
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
		Painter.fill(level, this, 2, Terrain.WALL);
		Painter.fill(level, this, 3, f);

		Point point;

		switch (Random.newInt(4)) {
			case 0: default: point = new Point(this.getWidth() / 2 + this.left, this.top + 2); break;
			case 1: point = new Point(this.getWidth() / 2 + this.left, this.bottom - 2); break;
			case 2: point = new Point(this.left + 2, this.getHeight() / 2 + this.top); break;
			case 3: point = new Point(this.right - 2, this.getHeight() / 2 + this.top); break;
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