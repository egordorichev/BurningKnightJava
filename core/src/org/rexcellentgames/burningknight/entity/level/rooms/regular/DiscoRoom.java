package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class DiscoRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 2, Terrain.DISCO);

		if (Random.chance(50)) {
			if (Random.chance(50)) {
				Painter.fill(level, this, 3, Terrain.randomFloor());
			} else {
				Painter.fill(level, this, 3, Terrain.randomFloor());
			}

			if (Random.chance(50)) {
				Painter.fill(level, this, 4, Terrain.DISCO);
			} else {
				Painter.fillEllipse(level, this, 4, Terrain.DISCO);
			}
		}
	}
}