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

		/*if (Dungeon.depth == 4) {
			Point center = this.getCenter();

			Boss boss = new CrazyKing();

			boss.x = center.x * 16 + (16 - boss.w) / 2;
			boss.y = center.y * 16 + (16 - boss.h) / 2 - 4;

			Dungeon.area.add(boss);
			LevelSave.add(boss);
		}*/
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

	@Override
	public int getMaxConnections(Connection side) {
		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}
}