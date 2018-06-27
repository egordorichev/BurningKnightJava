package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BigHoleWithRectRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 2, Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);

		byte f = Terrain.randomFloor();

		// ideas: plus room
		// grid room?

		int w =  this.getWidth();
		Painter.drawLine(level, new Point(this.left + w / 2, this.top + 1), new Point(this.left + w / 2, this.top + this.getHeight() / 2), f);
		Painter.fill(level, this, Math.min(this.getWidth() / 2, this.getHeight() / 2) - 1, f);
	}

	@Override
	public int getMinWidth() {
		return 7;
	}

	@Override
	public int getMinHeight() {
		return 7;
	}
}