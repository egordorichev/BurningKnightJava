package org.rexcellentgames.burningknight.entity.level.rooms.ladder;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class LineCircleRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		byte floor = Terrain.randomFloor();
		byte fl = Random.chance(30) ? Terrain.WALL : (Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);

		if (fl == Terrain.LAVA) {
			floor = Random.chance(40) ? Terrain.WATER : Terrain.DIRT;
		}

		Painter.fill(level, this, fl);
		Painter.fill(level, this, 1, floor);

		Painter.fillEllipse(level, this, 2, fl);
		Painter.fillEllipse(level, this, 3, floor);

		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), floor);
		Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), floor);
		Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), floor);
		Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), floor);

		place(level, this.getCenter());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
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