package org.rexcellentgames.burningknight.entity.level.rooms;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;

public class PrebossRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);
		Painter.fill(level, this, 1, Terrain.FLOOR_D);
	}
}