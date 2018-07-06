package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class CornerRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Random.chance(50) ? Terrain.WALL : Terrain.CHASM;

		Painter.set(level, new Point(this.left + 1, this.top + 1), f);
		Painter.set(level, new Point(this.right - 1, this.top + 1), f);
		Painter.set(level, new Point(this.left + 1, this.bottom - 1), f);
		Painter.set(level, new Point(this.right - 1, this.bottom - 1), f);

		Painter.fill(level, this, 2, Terrain.randomFloor());
		Painter.fillEllipse(level, this, Random.newInt(2, 4), Terrain.randomFloor());

		if (Random.chance(50)) {
			paintTunnel(level, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloor());
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (p.x == this.left + 1 && (p.y == this.top + 1 || p.y == this.bottom - 1)) {
			return false;
		}

		if (p.x == this.right - 1 && (p.y == this.top + 1 || p.y == this.bottom - 1)) {
			return false;
		}

		return super.canConnect(p);
	}
}