package org.rexellentgames.dungeon.entity.level.rooms.ladder;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LineCircleRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		byte floor = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, floor);

		Painter.fillEllipse(level, this, 2, Terrain.WALL);
		Painter.fillEllipse(level, this, 3, floor);

		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), floor);
		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), floor);
		Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), floor);
		Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), floor);

		place(level, this.getCenter());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
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