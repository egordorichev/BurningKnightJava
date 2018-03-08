package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class CircleRoom extends RegularRoom {
	public CircleRoom() {
		super(Type.CIRCLE_ROOM);
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fillEllipse(level, this, 1, Terrain.FLOOR);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);

			if (door.x == left) {
				Painter.set(level, (int) door.x + 1, (int) door.y, Terrain.FLOOR);
				Painter.set(level, (int) door.x + 2, (int) door.y, Terrain.FLOOR);
			} else if (door.x == right) {
				Painter.set(level, (int) door.x - 1, (int) door.y, Terrain.FLOOR);
				Painter.set(level, (int) door.x - 2, (int) door.y, Terrain.FLOOR);
			} else if (door.y == top) {
				Painter.set(level, (int) door.x, (int) door.y + 1, Terrain.FLOOR);
				Painter.set(level, (int) door.x, (int) door.y + 2, Terrain.FLOOR);
			} else if (door.y == bottom) {
				Painter.set(level, (int) door.x, (int) door.y - 1, Terrain.FLOOR);
				Painter.set(level, (int) door.x, (int) door.y - 2, Terrain.FLOOR);
			}
		}
	}
}