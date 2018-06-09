package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class CenterWallRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Random.chance(40) ? Terrain.CHASM : Terrain.WALL;

		Painter.drawLine(level, new Point(this.left + this.getWidth() / 2 - 2, this.top + this.getHeight() / 2 + 1),
			new Point(this.left + this.getWidth() / 2 - 2, this.top + 1), f);

		Painter.drawLine(level, new Point(this.left + this.getWidth() / 2 + 2, this.top + this.getHeight() / 2 - 1),
			new Point(this.left + this.getWidth() / 2 + 2, this.bottom - 1), f);

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.left + this.getWidth() / 2 - 1, this.top + this.getHeight() / 2 + 1), f);
			Painter.set(level, new Point(this.left + this.getWidth() / 2 + 1, this.top + this.getHeight() / 2 - 1), f);
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