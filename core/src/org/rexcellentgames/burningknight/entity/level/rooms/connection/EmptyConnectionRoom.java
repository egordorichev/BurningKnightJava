package org.rexcellentgames.burningknight.entity.level.rooms.connection;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class EmptyConnectionRoom extends TunnelRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.randomFloor());

		paintTunnel(level, Random.chance(50) ? Terrain.WALL : Terrain.CHASM, true);
		paintTunnel(level, Terrain.randomFloor());

		for (Door door : connected.values()) {
			door.setType(Door.Type.TUNNEL);
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
}