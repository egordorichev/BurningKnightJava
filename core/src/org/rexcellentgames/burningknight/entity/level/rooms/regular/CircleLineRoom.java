package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class CircleLineRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}

		byte floor = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, floor);

		Painter.fillEllipse(level, this, 2, Terrain.WALL);
		Painter.fillEllipse(level, this, 3, floor);

		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), floor);
		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), floor);
		Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), floor);
		Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), floor);
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}
}