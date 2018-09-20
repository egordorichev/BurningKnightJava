package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class FilledRombRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		double h = getHeight();
		double w = getWidth();
		int hh = getHeight() / 2;
		int ww = getWidth() / 2;

		byte floor = Random.chance(33) ? Terrain.WALL : (Random.chance(50) ? Terrain.LAVA : Terrain.CHASM);
		byte fix = (floor == Terrain.LAVA ? (Random.chance(50) ? Terrain.WATER : Terrain.DIRT) : Terrain.randomFloor());

		boolean a = Random.chance(30);

		if (a || Random.chance(50)) {
			Painter.triangle(level, new Point(this.left + 1, this.top + (int) Math.ceil(h / 2) + 1),
				new Point(this.left + 1, this.bottom - 1), new Point(this.left + (int) Math.floor(w / 2) - 1, this.bottom - 1), floor);
		}

		if (a || Random.chance(50)) {
			Painter.triangle(level, new Point(this.left + 1, this.top + (int) Math.floor(h / 2) - 1),
				new Point(this.left + 1, this.top + 1), new Point(this.left + (int) Math.floor(w / 2) - 1, this.top + 1), floor);
		}

		if (a || Random.chance(50)) {
			Painter.triangle(level, new Point(this.right - (int) Math.floor((w - 0.5) / 2) + 1, this.bottom - 1),
				new Point(this.right - 1, this.top + (int) Math.ceil(h / 2) + 1), new Point(this.right - 1, this.bottom), floor);
		}

		if (a || Random.chance(50)) {
			Painter.triangle(level, new Point(this.right - (int) Math.floor((w - 0.5) / 2) + 1, this.top + 1),
				new Point(this.right - 1, this.top + 1), new Point(this.right - 1, this.top + (int) Math.floor(h / 2) - 1), floor);
		}

		float r = Random.newFloat(1);
		floor = Random.chance(50) ? Terrain.WALL : (Random.chance(30) ? Terrain.LAVA : Terrain.CHASM);

		if (r < 0.33f) {
			int m = Random.newInt(2, 5) + 2;

			if (Random.chance(50)) {
				Painter.fillEllipse(level, this, m - Random.newInt(1, 3), fix);
				Painter.fillEllipse(level, this, m + 1, floor);
			} else {
				Painter.fill(level, this, m - Random.newInt(1, 3), fix);
				Painter.fill(level, this, m, floor);
			}
		} else if (r < 0.66f) {
			int m = Random.newInt(1, 3);
			Rect rect = this.shrink((int) ((ww - 0.5) / 2 + m), (int) ((hh - 0.5) / 2 + m));

			if (Random.chance(50)) {
				Painter.fillEllipse(level, rect, floor);
			} else {
				Painter.fill(level, rect, floor);
			}
		}
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}

	@Override
	public int getMinHeight() {
		return 10;
	}

	@Override
	public int getMinWidth() {
		return 10;
	}

	@Override
	public int getMaxHeight() {
		return 24;
	}

	@Override
	public int getMaxWidth() {
		return 24;
	}

	@Override
	public boolean canConnect(Point p) {
		if (!(p.x == this.left + this.getWidth() / 2 && p.y == this.top)
			&& !(p.x == this.left + this.getWidth() / 2 && p.y == this.bottom)
			&& !(p.x == this.left && p.y == this.top + this.getHeight() / 2)
			&& !(p.x == this.right && p.y == this.top + this.getHeight() / 2)) {
			return false;
		}

		return super.canConnect(p);
	}
}