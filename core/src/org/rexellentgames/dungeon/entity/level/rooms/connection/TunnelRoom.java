package org.rexellentgames.dungeon.entity.level.rooms.connection;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;

public class TunnelRoom extends ConnectionRoom {
	protected void fill(Level level) {

	}

	@Override
	public void paint(Level level) {
		this.fill(level);
		this.paintTunnel(level, Terrain.randomFloor());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.TUNNEL);
		}
	}
}