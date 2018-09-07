package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class BigHoleRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			Painter.fill(level, this, Random.newInt(4, 8), Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);
		} else {
			Painter.fillEllipse(level, this, Random.newInt(4, 8), Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);
		}
	}
}