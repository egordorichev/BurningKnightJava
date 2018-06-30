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

		byte f = Terrain.CHASM;
		byte fl = Terrain.randomFloor();

		Painter.fill(level, new Rect(this.left + 2, this.top + this.getHeight() / 2 - 2, this.right - 1, this.top + this.getHeight() / 2 + 2), f);
		Painter.fill(level, new Rect(this.left + this.getWidth() / 2 - 2, this.top + 2, this.left + this.getWidth() / 2 + 2, this.bottom - 1), f);

		if (Random.chance(50)) {
			int side = Random.newInt(4);

			Painter.fill(level, new Rect(this.left + (side == 0 ? 2 : 3), this.top + this.getHeight() / 2 - 1, this.right - (side == 1 ? 2 : 3), this.top + this.getHeight() / 2 + 1), fl);
			Painter.fill(level, new Rect(this.left + this.getWidth() / 2 - 1, this.top + (side == 2 ? 2 : 3), this.left + this.getWidth() / 2 + 1, this.bottom - (side == 3 ? 1 : 2)), fl);
		}
	}
}