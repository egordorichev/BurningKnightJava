package org.rexcellentgames.burningknight.entity.level.rooms.entrance;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.BossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BossEntranceRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);
		Painter.fillEllipse(level, this, 1, Terrain.FLOOR_D);
	}

	@Override
	public int getMinWidth() {
		return 7;
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMaxHeight() {
		return 10;
	}

	@Override
	public int getMaxWidth() {
		return 10;
	}

	@Override
	public boolean canConnect(Room r) {
		return r instanceof BossRoom && super.canConnect(r);
	}

	protected void place(Level level, Point point) {
		Painter.set(level, (int) point.x, (int) point.y, Terrain.randomFloor());

		Exit exit = new Exit();

		exit.x = point.x * 16;
		exit.y = point.y * 16 - 8;

		level.set((int) point.x, (int) point.y, Terrain.EXIT);
		LevelSave.add(exit);
		Dungeon.area.add(exit);
	}
}