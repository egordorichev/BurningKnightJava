package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;

public class CollumnRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 3, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
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