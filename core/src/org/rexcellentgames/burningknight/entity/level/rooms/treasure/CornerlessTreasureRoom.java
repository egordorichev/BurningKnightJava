package org.rexcellentgames.burningknight.entity.level.rooms.treasure;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class CornerlessTreasureRoom extends TreasureRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.FLOOR_D);

		byte f = Random.chance(50) ? Terrain.WALL : Terrain.CHASM;

		Painter.set(level, new Point(this.left + 1, this.top + 1), f);
		Painter.set(level, new Point(this.right - 1, this.top + 1), f);
		Painter.set(level, new Point(this.left + 1, this.bottom - 1), f);
		Painter.set(level, new Point(this.right - 1, this.bottom - 1), f);

		Painter.fill(level, this, 2, Terrain.randomFloor());

		if (Random.chance(50)) {
			Painter.fill(level, this, 3, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloor());
		} else {
			Painter.fillEllipse(level, this, 3, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloor());
		}

		placeChest(this.getCenter());
	}


	@Override
	public boolean canConnect(Point p) {
		if (p.x == this.left + 1 && (p.y == this.top || p.y == this.bottom)) {
			return false;
		}

		if (p.x == this.right - 1 && (p.y == this.top || p.y == this.bottom)) {
			return false;
		}

		if (p.y == this.top + 1 && (p.x == this.left || p.x == this.right)) {
			return false;
		}

		if (p.y == this.bottom - 1 && (p.x == this.left || p.x == this.right)) {
			return false;
		}

		return super.canConnect(p);
	}
}