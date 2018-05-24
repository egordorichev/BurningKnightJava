package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Patch;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class ChasmRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR_A);

		int w = this.getWidth() - 2;

		boolean[] patch = Patch.generate(w, this.getHeight() - 2, 0.6f, 4);

		for (int x = 2; x < this.getWidth() - 2; x++) {
			for (int y = 2; y < this.getHeight() - 2; y++) {
				if (patch[x + y * w]) {
					Painter.set(level, this.left + x, this.top + y, Terrain.CHASM);
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}