package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class TreasureRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 2, Terrain.WOOD);
	}
}