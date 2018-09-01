package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class AnticrossRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Random.chance(33) ? Terrain.WALL : (Random.chance(50) ? Terrain.LAVA : Terrain.CHASM);
		byte fl = Terrain.randomFloor();

		if (f == Terrain.LAVA) {
			fl = Random.chance(50) ? Terrain.WATER : Terrain.DIRT;
		}

		int w = Random.newInt(2, this.getWidth() / 2 - 4);
		int h = Random.newInt(2, this.getHeight() / 2 - 4);

		Painter.fill(level, new Rect(this.left + 2, this.top + this.getHeight() / 2 - w, this.right - 1, this.top + this.getHeight() / 2 + w), f);
		Painter.fill(level, new Rect(this.left + this.getWidth() / 2 - h, this.top + 2, this.left + this.getWidth() / 2 + 2, this.bottom - 1), f);

		if (Random.chance(50)) {
			int side = Random.newInt(4);

			w -= 1;
			h -= 1;

			Painter.fill(level, new Rect(this.left + 2 /* (side == 0 ? 2 : 3) */, this.top + this.getHeight() / 2 - w, this.right - (side == 1 ? 2 : 3), this.top + this.getHeight() / 2 + w), fl);
			Painter.fill(level, new Rect(this.left + this.getWidth() / 2 - h, this.top + (side == 2 ? 2 : 3), this.left + this.getWidth() / 2 + h, this.bottom - (side == 3 ? 1 : 2)), fl);

			if (f != Terrain.LAVA) {
				fl = Terrain.randomFloor();
			}

			w -= 1;
			h -= 1;

			Painter.fill(level, new Rect(this.left + (side == 0 ? 2 : 3), this.top + this.getHeight() / 2 - w, this.right - (side == 1 ? 2 : 3), this.top + this.getHeight() / 2 + w), fl);
			Painter.fill(level, new Rect(this.left + this.getWidth() / 2 - h, this.top + (side == 2 ? 2 : 3), this.left + this.getWidth() / 2 + h, this.bottom - (side == 3 ? 1 : 2)), fl);
		}
	}
}