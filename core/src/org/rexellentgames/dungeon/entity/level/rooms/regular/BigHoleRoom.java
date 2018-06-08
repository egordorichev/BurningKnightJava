package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;

public class BigHoleRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			Painter.fill(level, this, 2, Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);
		} else {
			Painter.fillEllipse(level, this, 2, Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);
		}
	}
}