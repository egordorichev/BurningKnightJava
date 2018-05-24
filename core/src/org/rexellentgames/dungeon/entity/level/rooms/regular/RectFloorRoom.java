package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class RectFloorRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int i = 1; i < this.getWidth() / 2; i++) {
			Painter.fill(level, this, i, i % 2 == 0 ? Terrain.FLOOR_B : Terrain.FLOOR_A);
		}
	}
}