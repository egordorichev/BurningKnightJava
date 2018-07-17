package org.rexcellentgames.burningknight.entity.level.rooms.entrance;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class CircleEntranceRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();
		Painter.fill(level, this, Terrain.WALL);
		Painter.fillEllipse(level, this, 1, f);
		Painter.fillEllipse(level, this, 2, Terrain.randomFloor());

		paintTunnel(level, Terrain.randomFloor());

		this.place(level, this.getCenter());

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
		return 5;
	}

	@Override
	public int getMinHeight() {
		return 5;
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