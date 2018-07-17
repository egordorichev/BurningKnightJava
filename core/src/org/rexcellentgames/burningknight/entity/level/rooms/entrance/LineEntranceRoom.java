package org.rexcellentgames.burningknight.entity.level.rooms.entrance;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class LineEntranceRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();
		byte fl = Random.chance(30) ? Terrain.WALL : (Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);
		Painter.fill(level, this, Terrain.WALL);

		if (fl == Terrain.LAVA) {
			f = Random.chance(40) ? Terrain.WATER : Terrain.DIRT;
		}

		Painter.fill(level, this, 1, Terrain.randomFloor());

		Painter.fill(level, this, 1, f);
		Painter.fill(level, this, 2, fl);
		Painter.fill(level, this, 3, f);

		boolean s = false;

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), f);
			s = true;
		}

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), f);
			s = true;
		}

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), f);
			s = true;
		}

		if (Random.chance(50) || !s) {
			Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), f);
		}

		if (Random.chance(50)) {
			f = Terrain.randomFloor();

			if (fl == Terrain.LAVA) {
				f = Terrain.DIRT;
			}

			if (Random.chance(50)) {
				Painter.fillEllipse(level, this, 4, f);
			} else {
				Painter.fill(level, this, 4, f);
			}
		}

		place(level, this.getCenter());

		byte flr = Terrain.randomFloor();

		if (Random.chance(50)) {
			Painter.fill(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.top + 4), flr);
			Painter.fill(level, new Rect(this.right - 3, this.top + 1, this.right, this.top + 4), flr);
			Painter.fill(level, new Rect(this.left + 1, this.bottom - 3, this.left + 4, this.bottom), flr);
			Painter.fill(level, new Rect(this.right - 3, this.bottom - 3, this.right, this.bottom), flr);
		} else {
			Painter.fillEllipse(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.top + 4), flr);
			Painter.fillEllipse(level, new Rect(this.right - 3, this.top + 1, this.right, this.top + 4), flr);
			Painter.fillEllipse(level, new Rect(this.left + 1, this.bottom - 3, this.left + 4, this.bottom), flr);
			Painter.fillEllipse(level, new Rect(this.right - 3, this.bottom - 3, this.right, this.bottom), flr);
		}
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}
}