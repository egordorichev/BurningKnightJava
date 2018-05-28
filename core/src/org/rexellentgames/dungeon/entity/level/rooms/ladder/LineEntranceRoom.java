package org.rexellentgames.dungeon.entity.level.rooms.ladder;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LineEntranceRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 2, Terrain.WALL);
		Painter.fill(level, this, 3, Terrain.FLOOR_A);

		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), Terrain.FLOOR_A);
		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), Terrain.FLOOR_A);
		Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), Terrain.FLOOR_A);
		Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), Terrain.FLOOR_A);
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