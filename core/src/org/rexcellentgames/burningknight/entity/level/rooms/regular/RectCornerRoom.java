package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class RectCornerRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);

		boolean chasm = Random.chance(50);

		if (chasm) {
			Painter.fill(level, this, 1, Terrain.CHASM);
		}

		Painter.fill(level, this, 2, f);
		Painter.fill(level, this, 3, chasm ? Terrain.CHASM : Terrain.WALL);

		Painter.fill(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.top + 4), f);
		Painter.fill(level, new Rect(this.right - 3, this.top + 1, this.right, this.top + 4), f);
		Painter.fill(level, new Rect(this.left + 1, this.bottom - 3, this.left + 4, this.bottom), f);
		Painter.fill(level, new Rect(this.right - 3, this.bottom - 3, this.right, this.bottom), f);

		for (Door door : connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (!(p.x == this.left + 2 && p.y == this.top)
			&& !(p.x == this.left && p.y == this.top + 2)
			&& !(p.x == this.right - 2 && p.y == this.top)
			&& !(p.x == this.right && p.y == this.top + 2)
			&& !(p.x == this.left + 2 && p.y == this.bottom)
			&& !(p.x == this.left && p.y == this.bottom - 2)
			&& !(p.x == this.right - 2 && p.y == this.bottom)
			&& !(p.x == this.right && p.y == this.bottom - 2)) {

			return false;
		}


		return super.canConnect(p);
	}
}