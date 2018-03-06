package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class TowerBaseRoom extends SpecialRoom {
	public TowerBaseRoom() {
		super(Type.TOWER_BASE);
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, 1, Terrain.LOW_GRASS);
		Point point = this.getCenter();
		Painter.set(level, (int) point.x, (int) point.y, Terrain.ENTRANCE);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
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
		return 6;
	}

	@Override
	public int getMaxHeight() {
		return 6;
	}
}