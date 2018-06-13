package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class RectFloorRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int i = 1; i < this.getWidth() / 2; i++) {
			Painter.fill(level, this, i, i % 2 == 0 ? Terrain.FLOOR_B : Terrain.FLOOR_A);
		}
	}
}