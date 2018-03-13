package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class GardenRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);

		if (Random.chance(50)) {
			for (int x = this.left; x < this.right; x++) {
				Painter.drawLine(level, new Point(x, this.top), new Point(x, this.bottom),
					x % 2 == 0 ? Terrain.GRASS : Terrain.WOOD);
			}
		} else {
			for (int y = this.top; y < this.bottom; y++) {
				Painter.drawLine(level, new Point(this.left, y), new Point(this.right, y),
					y % 2 == 0 ? Terrain.GRASS : Terrain.WOOD);
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}