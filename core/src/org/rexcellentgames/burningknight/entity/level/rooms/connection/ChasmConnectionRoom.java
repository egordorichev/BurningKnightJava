package org.rexcellentgames.burningknight.entity.level.rooms.connection;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class ChasmConnectionRoom extends ConnectionRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.randomFloor());

		if (Random.chance(50)) {
			Painter.fill(level, this, 2, Random.chance(50) ? Terrain.WALL : Terrain.CHASM);
		} else {
			Painter.fillEllipse(level, this, 2, Random.chance(50) ? Terrain.WALL : Terrain.CHASM);
		}
	}
}