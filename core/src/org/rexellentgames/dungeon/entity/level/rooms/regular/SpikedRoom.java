package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;

public class SpikedRoom extends RegularRoom {
	public SpikedRoom() {
		super(Type.SPIKED);
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR);

		if (this.getWidth() > 5 && this.getHeight() > 5) {
			Painter.fill(level, this, 2, Terrain.SPIKES);
			Painter.fill(level, this, 3, Terrain.FLOOR);

			// todo: moving spike?
			Painter.set(level, this.left + this.getWidth() / 2, Random.chance(50) ? this.top + 2 : this.bottom - 2, Terrain.FLOOR);
		}
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMinWidth() {
		return 7;
	}

	@Override
	public int getMaxWidth() {
		return 10;
	}

	@Override
	public int getMaxHeight() {
		return 10;
	}
}