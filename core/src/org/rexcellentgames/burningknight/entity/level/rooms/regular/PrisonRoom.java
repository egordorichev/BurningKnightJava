package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class PrisonRoom extends RegularRoom {
	@Override
	public int getMinWidth() {
		return 14;
	}

	@Override
	public int getMaxWidth() {
		return 30;
	}

	@Override
	public int getMinHeight() {
		return 14;
	}

	@Override
	public int getMaxHeight() {
		return 24;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int cellH = (int) Math.ceil(((float) this.getHeight()) / 3) + 1;
		byte f = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, new Rect(this.left + 1, this.top + cellH, this.right, this.bottom - cellH + 1), f);

		if (Random.chance(50)) {
			Painter.fill(level, new Rect(this.left + 1, this.top + cellH, this.right, this.bottom - cellH + 1), 1, Terrain.randomFloor());
		}

		for (int y = 0; y < 2; y++) {
			for (int x = 1; x < this.getWidth(); x += 0) {
				int w = Random.newInt(6, 8);

				if (w + x + 1 + this.left < this.right) {
					paintSub(level, this.left + x, y == 0 ? this.top : (this.bottom - cellH + 1), w + 1, cellH, y == 0);
				}

				x += w;
			}
		}
	}

	private void paintSub(Level level, int x, int y, int w, int h, boolean top) {
		byte f = Terrain.randomFloor();
		Rect rc = new Rect(x, y, x + w, y + h);

		Painter.fill(level, rc, 1, f);

		int dx = Random.newInt(x + 2, x + w - 2);
		int dy = y;

		if (top) {
			dy = y + h - 1;
		}

		Painter.set(level, dx, dy, f);

		int r = Random.newInt(6);

		if (r == 0) {
			paintDead(level, rc, top);
		} else if (r == 1) {
			paintCollumn(level, rc, top);
		} else if (r == 2) {
			paintLava(level, rc, top);
		} else if (r == 3) {
			paintCircle(level, rc, top);
		} else {
			paintNormal(level, rc, top);
		}
	}

	private void paintPatch(Level level, Rect self, boolean top) {

	}

	private void paintCircle(Level level, Rect self, boolean top) {
		byte f = Terrain.randomFloor();
		Painter.fill(level, self, 1, Terrain.FLOOR_A);
		Painter.fill(level, self, 1, Random.chance(50) ? Terrain.WALL : (Random.chance(50) ? Terrain.LAVA : Terrain.FLOOR_D));
		Painter.fillEllipse(level, self, 1, f);

		f = Terrain.FLOOR_D;
	}

	private void paintLava(Level level, Rect self, boolean top) {
		Painter.fill(level, self, 1, Terrain.LAVA);

		if (Random.chance(50)) {
			byte f = Terrain.DIRT;
			Painter.fill(level, self, 2, f);

			int m = top ? 1 : -1;

			Painter.fill(level, new Rect(self.left, self.top + m, self.right, self.bottom + m), 2, f);
		}
	}

	private void paintDead(Level level, Rect self, boolean top) {
		Painter.fill(level, self, Terrain.WALL);
	}

	private void paintCollumn(Level level, Rect self, boolean top) {
		Painter.fill(level, self, 2, Random.chance(50) ? Terrain.LAVA : Terrain.WALL);
	}

	private void paintNormal(Level level, Rect self, boolean top) {

	}

	@Override
	public boolean canConnect(Point p) {
		int cellH = (int) Math.ceil(((float) this.getHeight()) / 3) + 1;

		if (p.y < this.top + cellH || p.y > this.bottom - cellH - 1) {
			return false;
		}

		return super.canConnect(p);
	}
}