package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class CircleRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);

		if (Random.chance(50)) {
			Painter.fill(level, this, 1, Terrain.CHASM);
		}

		Painter.fillEllipse(level, this, 1, f);

		if (Random.chance(50)) {
			Painter.fillEllipse(level, this, 3, Random.chance(33) ? Terrain.WALL : (Random.chance(50) ? Terrain.CHASM : Terrain.LAVA));
		}

		paintTunnel(level, Terrain.randomFloor());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}