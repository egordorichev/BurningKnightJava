package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class RectFloorRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f1 = Terrain.randomFloor();
		byte f2 = Terrain.randomFloor();

		while (f2 == f1) {
			f2 = Terrain.randomFloor();
		}

		for (int i = 1; i < this.getWidth() / 2; i++) {
			Painter.fill(level, this, i, i % 2 == 0 ? f1 : f2);
		}
	}
}