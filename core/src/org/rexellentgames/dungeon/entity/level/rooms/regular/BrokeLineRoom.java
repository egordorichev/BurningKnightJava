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

		byte f = Terrain.randomFloor();
		
		Painter.fill(level, this, 2, Random.chance(50) ? Terrain.WALL : Terrain.LAVA);
		Painter.fill(level, this, 3, f);

		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), f);
		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), f);
		Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), f);
		Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), f);

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