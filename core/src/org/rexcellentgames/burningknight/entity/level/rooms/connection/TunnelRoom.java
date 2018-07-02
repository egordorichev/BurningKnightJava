package org.rexcellentgames.burningknight.entity.level.rooms.connection;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.util.Random;

public class TunnelRoom extends ConnectionRoom {
	protected void fill(Level level) {

	}

	@Override
	public void paint(Level level) {
		this.fill(level);

		boolean bold = Random.chance(20);
		this.paintTunnel(level, this instanceof SpikedTunnelRoom ? Terrain.DIRT : Terrain.randomFloor(), bold);

		if (bold && !(this instanceof SpikedTunnelRoom)) {
			this.paintTunnel(level, Terrain.randomFloor());
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.TUNNEL);
		}
	}
}