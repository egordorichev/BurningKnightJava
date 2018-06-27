package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class SpikedRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		if (this.getWidth() > 5 && this.getHeight() > 5) {
			Painter.fill(level, this, 2, Terrain.LAVA);
			Painter.fill(level, this, 3, Terrain.DIRT);

			Painter.set(level, this.left + this.getWidth() / 2, Random.chance(50) ? this.top + 2 : this.bottom - 2, Terrain.DIRT);
		}
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMinWidth() {
		return 7;
	}

	@Override
	public int getMaxWidth() {
		return 10;
	}

	@Override
	public int getMaxHeight() {
		return 10;
	}
}