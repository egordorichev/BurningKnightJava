package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.connection.*;
import org.rexellentgames.dungeon.entity.level.rooms.regular.CircleRoom;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class ConnectionRoomPool extends Pool<ConnectionRoom> {
	public static ConnectionRoomPool instance = new ConnectionRoomPool();

	public ConnectionRoomPool() {
		this.add(TunnelRoom.class, 5);
		this.add(ChasmTunnelRoom.class, 3);
		this.add(RingConnectionRoom.class, 3);
		this.add(SpikedTunnelRoom.class, 2);
		this.add(CircleRoom.class, 1);
	}
}