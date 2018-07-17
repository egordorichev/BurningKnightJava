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
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BossEntranceRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			Painter.fill(level, this, 1, Terrain.randomFloor());
		} else {
			Painter.fillEllipse(level, this, 1, Terrain.randomFloor());
		}

		if (Random.chance(50)) {
			int m = Random.newInt(2, 4);

			if (Random.chance(50)) {
				Painter.fill(level, this, m, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
				Painter.fill(level, this, m + 1, Terrain.randomFloor());
			} else {
				Painter.fillEllipse(level, this, m, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
				Painter.fillEllipse(level, this, m + 1, Terrain.randomFloor());
			}
		}

		paintTunnel(level, Terrain.FLOOR_D);

		this.place(level, this.getCenter());
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	@Override
	public int getMaxHeight() {
		return 12;
	}

	@Override
	public int getMaxWidth() {
		return 12;
	}

	@Override
	public boolean canConnect(Room r) {
		return r instanceof BossRoom && super.canConnect(r);
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}

	protected void place(Level level, Point point) {
		Exit exit = new Exit();

		exit.x = point.x * 16;
		exit.y = point.y * 16 - 8;

		level.set((int) point.x, (int) point.y, Terrain.EXIT);
		LevelSave.add(exit);
		Dungeon.area.add(exit);
	}
}