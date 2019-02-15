package org.rexcellentgames.burningknight.entity.level.rooms;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class PrebossRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);
		Painter.fill(level, this, 1, Terrain.FLOOR_D);

		if (Random.chance(50)) {
			byte fl = Random.chance(50) ? Terrain.CHASM : Terrain.WALL;
			Painter.fill(level, this, 2, fl);

			Painter.fill(level, this, 2 + Random.newInt(1, 4), Terrain.FLOOR_D);
		}

		if (this.getWidth() > 4 && this.getHeight() > 4 && Random.chance(50)) {
			this.paintTunnel(level, Terrain.FLOOR_D, true);
			this.paintTunnel(level, Terrain.randomFloor());
		} else {
			this.paintTunnel(level, Terrain.FLOOR_D);
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
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
	public int getMaxWidth() {
		return 8;
	}

	@Override
	public int getMaxHeight() {
		return 8;
	}
}