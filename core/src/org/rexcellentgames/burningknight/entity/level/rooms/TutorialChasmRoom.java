package org.rexcellentgames.burningknight.entity.level.rooms;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class TutorialChasmRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		// Door door = this.getConnected().values().iterator().next();
		Painter.drawLine(level, new Point(left + getWidth() / 2, bottom - 1), new Point(left + getWidth() / 2, top + 1), Terrain.CHASM);
	}
}