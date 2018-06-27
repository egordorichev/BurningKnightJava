package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.entity.pool.room.SecretRoomPool;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.entity.pool.room.SecretRoomPool;

public class SecretRoom extends Room {
	@Override
	public void paint(Level level) {
		hidden = true;

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR_D);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.SECRET);
		}
	}

	@Override
	public int getMinWidth() {
		return 5;
	}

	public int getMaxWidth() {
		return 10;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	public int getMaxHeight() {
		return 10;
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}

	public static SecretRoom create() {
		return SecretRoomPool.instance.generate();
	}

	@Override
	public boolean canConnect(Room r) {
		return !(r instanceof ConnectionRoom) && super.canConnect(r);
	}
}