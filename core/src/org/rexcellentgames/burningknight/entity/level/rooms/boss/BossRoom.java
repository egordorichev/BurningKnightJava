package org.rexcellentgames.burningknight.entity.level.rooms.boss;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;

public class BossRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.randomFloor());

		for (Door door : connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	public int getMinWidth() {
		return 18;
	}

	@Override
	public int getMinHeight() {
		return 18;
	}

	@Override
	public int getMaxWidth() {
		return 26;
	}

	@Override
	public int getMaxHeight() {
		return 26;
	}
}