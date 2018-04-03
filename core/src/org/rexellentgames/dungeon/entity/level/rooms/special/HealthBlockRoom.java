package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.HealthBlocker;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class HealthBlockRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.WOOD);

		Point center = this.getCenter();

		Painter.set(level, center, Terrain.DIRT);
		HealthBlocker blocker = new HealthBlocker();

		blocker.x = center.x * 16;
		blocker.y = center.y * 16;

		Dungeon.area.add(blocker);
		Dungeon.level.addSaveable(blocker);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	public int getMaxHeight() {
		return 6;
	}

	@Override
	public int getMaxWidth() {
		return 6;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	@Override
	public int getMinWidth() {
		return 5;
	}
}