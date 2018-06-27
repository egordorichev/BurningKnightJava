package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class LavaLakeRoom extends PatchRoom {
	@Override
	protected float[] getSizeChance() {
		return new float[]{1, 3, 6};
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.LAVA);

		float fill = 0.5f + (this.getWidth() * this.getHeight()) / 512f;

		setupPatch(level, fill, 20, true);
		cleanDiagonalEdges();

		byte floor = Terrain.randomFloor();

		for (int i = top + 1; i < bottom; i++) {
			for (int j = left + 1; j < right; j++) {
				int in = xyToPatchCoords(j, i);

				if (!this.patch[in]) {
					level.set(j, i, floor);
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}

	}
}