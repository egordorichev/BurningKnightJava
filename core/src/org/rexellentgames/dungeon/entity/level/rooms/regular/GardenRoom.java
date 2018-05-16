package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.plant.Cabbage;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class GardenRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR);

		if (Random.chance(50)) {
			for (int x = this.left + 1; x < this.right - 1; x++) {
				Painter.drawLine(level, new Point(x, this.top + 1), new Point(x, this.bottom - 1),
					x % 2 == 0 ? Terrain.DIRT : Terrain.WOOD);

				if (x % 2 == 0) {
					for (int y = this.top + 1; y < this.bottom; y++) {
						if (Random.chance(80)) {
							Cabbage cabbage = new Cabbage();

							cabbage.x = x * 16;
							cabbage.y = y * 16;
							cabbage.grow();

							Dungeon.area.add(cabbage);
							Dungeon.level.addSaveable(cabbage);
						}
					}
				}
			}
		} else {
			for (int y = this.top + 1; y < this.bottom - 1; y++) {
				Painter.drawLine(level, new Point(this.left + 1, y), new Point(this.right, y),
					y % 2 == 0 ? Terrain.DIRT : Terrain.WOOD);


				if (y % 2 == 0) {
					for (int x = this.left + 1; x < this.right - 1; x++) {
						if (Random.chance(50)) {
							Cabbage cabbage = new Cabbage();

							cabbage.x = x * 16;
							cabbage.y = y * 16;
							cabbage.grow();

							Dungeon.area.add(cabbage);
							Dungeon.level.addSaveable(cabbage);
						}
					}
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}