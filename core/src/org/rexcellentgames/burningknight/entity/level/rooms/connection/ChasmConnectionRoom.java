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

			if (Random.chance(50)) {
				if (Random.chance(50)) {
					Painter.fill(level, this, 3, Random.chance(50) ? Terrain.randomFloor()
						: (Random.chance(50) ? Terrain.WALL : Terrain.CHASM));
				} else {
					Painter.fillEllipse(level, this, 3, Random.chance(50) ? Terrain.randomFloor()
						: (Random.chance(50) ? Terrain.WALL : Terrain.CHASM));
				}

				if (Random.chance(50)) {
					Painter.fillEllipse(level, this, 4, Random.chance(50) ? Terrain.randomFloor()
						: (Random.chance(50) ? Terrain.WALL : Terrain.CHASM));
				}
			}
		} else {
			Painter.fillEllipse(level, this, 2, Random.chance(50) ? Terrain.WALL : Terrain.CHASM);

			if (Random.chance(50)) {
				Painter.fill(level, this, 3, Random.chance(50) ? Terrain.randomFloor()
					: (Random.chance(50) ? Terrain.WALL : Terrain.CHASM));
			}

			if (Random.chance(50)) {
				Painter.fillEllipse(level, this, 4, Random.chance(50) ? Terrain.randomFloor()
					: (Random.chance(50) ? Terrain.WALL : Terrain.CHASM));
			}
		}
	}

	@Override
	public int getMinWidth() {
		return 5;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}
}