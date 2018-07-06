package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RectMazeRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		int max = Math.min(this.getWidth(), this.getHeight()) / 2 + 1;

		for (int m = 2; m < max; m++) {
			if (m % 2 == 0) {
				Painter.fill(level, this, m, Random.chance(30) ? Terrain.WALL : Terrain.CHASM);

				if (m < max - 1) {
					for (int i = 0; i < (Random.chance(30) ? 2 : 1); i++) {
						byte f = Terrain.randomFloor();

						if (Random.chance(50)) {
							if (Random.chance(50)) {
								Painter.set(level, new Point(this.right - m, Random.newInt(this.top + 1 + m, this.bottom - m)), f);
							} else {
								Painter.set(level, new Point(this.left + m, Random.newInt(this.top + 1 + m, this.bottom - m)), f);
							}
						} else {
							if (Random.chance(50)) {
								Painter.set(level, new Point(Random.newInt(this.left + m + 1, this.right - m), this.top + m), f);
							} else {
								Painter.set(level, new Point(Random.newInt(this.left + m + 1, this.right - m), this.bottom - m), f);
							}
						}
					}
				}
			} else {
				Painter.fill(level, this, m, Terrain.randomFloor());
			}
		}
	}

	@Override
	protected boolean quad() {
		return Random.chance(30);
	}

	@Override
	protected int validateWidth(int w) {
		return w % 2 == 0 ? w : w - 1;
	}

	@Override
	protected int validateHeight(int h) {
		return h % 2 == 0 ? h : h - 1;
	}

	@Override
	public int getMaxHeight() {
		return 20;
	}

	@Override
	public int getMaxWidth() {
		return 20;
	}
}