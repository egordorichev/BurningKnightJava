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
			for (int x = this.left + 1; x < this.right - 1; x++) {
				Painter.drawLine(level, new Point(x, this.top + 1), new Point(x, this.bottom - 1),
					x % 2 == 0 ? Terrain.GRASS : Terrain.WOOD);
			}
		} else {
			for (int y = this.top + 1; y < this.bottom - 1; y++) {
				Painter.drawLine(level, new Point(this.left + 1, y), new Point(this.right - 1, y),
					y % 2 == 0 ? Terrain.GRASS : Terrain.WOOD);
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}