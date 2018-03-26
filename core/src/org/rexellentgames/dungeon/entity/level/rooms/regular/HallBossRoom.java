package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Patch;
import org.rexellentgames.dungeon.entity.level.Terrain;

public class HallBossRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		boolean[] patch = Patch.generate(this.getWidth(), this.getHeight(), 0.4f, 4);

		for (int x = 1; x < this.getWidth() - 1; x++) {
			for (int y = 1; y < this.getHeight() - 1; y++) {
				if (patch[x + y * this.getWidth()]) {
					level.set(this.left + x, this.top + y, Terrain.WOOD);
				}
			}
		}
	}

	@Override
	public int getMinWidth() {
		return 25;
	}

	@Override
	public int getMaxWidth() {
		return 26;
	}

	@Override
	public int getMinHeight() {
		return 25;
	}

	@Override
	public int getMaxHeight() {
		return 26;
	}
}