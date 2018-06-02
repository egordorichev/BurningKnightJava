package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BrokeLineRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 2, Random.chance(50) ? Terrain.WALL : Terrain.LAVA);
		Painter.fill(level, this, 3, Terrain.FLOOR_A);

		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), Terrain.FLOOR_A);
		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), Terrain.FLOOR_A);
		Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), Terrain.FLOOR_A);
		Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), Terrain.FLOOR_A);

		for (Door door : connected.values()) {
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