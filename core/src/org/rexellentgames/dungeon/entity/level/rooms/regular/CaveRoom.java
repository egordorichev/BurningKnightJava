package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class CaveRoom extends PatchRoom {
	@Override
	protected float[] getSizeChance() {
		return new float[]{1, 3, 6};
	}

	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();
		Painter.fill(level, this, Terrain.WALL);

		float fill = 0.1f + (this.getWidth() * this.getHeight()) / 512f;

		setupPatch(level, fill, 20, true);
		cleanDiagonalEdges();

		for (int i = top + 1; i < bottom; i++) {
			for (int j = left + 1; j < right; j++) {
				int in = xyToPatchCoords(j, i);

				if (!this.patch[in]) {
					level.set(j, i, f);
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}