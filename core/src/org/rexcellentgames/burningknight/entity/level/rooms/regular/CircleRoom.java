package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.TunnelRoom;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.TunnelRoom;

public class CircleRoom extends TunnelRoom {
	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);
		Painter.fillEllipse(level, this, 1, f);

		super.paint(level);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}
}