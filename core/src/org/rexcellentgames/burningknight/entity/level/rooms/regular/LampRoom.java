package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.pool.room.LampRoomPool;

public class LampRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		RegularRoom room = LampRoomPool.instance.generate();

		room.size = this.size;
		room.left = this.left;
		room.right = this.right;
		room.top = this.top;
		room.bottom = this.bottom;
		room.neighbours = this.neighbours;
		room.connected = this.connected;

		room.paint(level);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.ENEMY);
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

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 2;
		}

		return 1;
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 2;
	}
}