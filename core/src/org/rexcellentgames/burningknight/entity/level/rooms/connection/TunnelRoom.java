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

		byte fl =  this instanceof SpikedTunnelRoom ? Terrain.DIRT :
			(Random.chance(25) ? (Random.chance(33) ? Terrain.CHASM : (Random.chance(50) ? Terrain.WALL : Terrain.LAVA))
			: Terrain.randomFloor());

		if (Random.chance(50)) {
			this.paintTunnel(level, fl, true);
		}

		this.paintTunnel(level, fl == Terrain.LAVA ? (Random.chance(50) ? Terrain.WATER : Terrain.DIRT) : Terrain.randomFloor());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.TUNNEL);
		}
	}
}