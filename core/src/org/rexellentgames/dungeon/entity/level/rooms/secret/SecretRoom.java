package org.rexellentgames.dungeon.entity.level.rooms.secret;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.connection.ConnectionRoom;
import org.rexellentgames.dungeon.entity.pool.room.SecretRoomPool;

public class SecretRoom extends Room {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR_D);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.SECRET);
		}
	}

	@Override
	public int getMinWidth() {
		return 7;
	}

	public int getMaxWidth() {
		return 15;
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	public int getMaxHeight() {
		return 15;
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