package org.rexellentgames.dungeon.entity.level.rooms.connection;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class ChasmTunnelRoom extends TunnelRoom {
	@Override
	protected void fill(Level level) {
		if (Dungeon.depth != -1) {
			Painter.fill(level, this, Terrain.WALL);
		}

		Painter.fill(level, this, 1, Terrain.CHASM);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMinWidth() {
		return 7;
	}
}