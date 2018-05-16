package org.rexellentgames.dungeon.entity.level.rooms.connection;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class SpikedTunnelRoom extends TunnelRoom {
	@Override
	protected void fill(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.LAVA);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}