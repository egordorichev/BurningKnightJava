package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LineRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 2, Terrain.WALL);
		Painter.fill(level, this, 3, Terrain.FLOOR);

		Point point;

		switch (Random.newInt(4)) {
			case 0: default: point = new Point(this.getWidth() / 2 + this.left, this.top + 2); break;
			case 1: point = new Point(this.getWidth() / 2 + this.left, this.bottom - 2); break;
			case 2: point = new Point(this.left + 2, this.getHeight() / 2 + this.top); break;
			case 3: point = new Point(this.right - 2, this.getHeight() / 2 + this.top); break;
		}

		Painter.set(level, point, Terrain.FLOOR);
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