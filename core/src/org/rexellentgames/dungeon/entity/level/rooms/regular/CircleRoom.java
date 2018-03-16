package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.connection.TunnelRoom;

import java.util.Map;

public class CircleRoom extends TunnelRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fillEllipse(level, this, 1, Terrain.FLOOR);

		super.paint(level);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}