package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.connection.*;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class ConnectionRoomPool extends Pool<ConnectionRoom> {
	public static ConnectionRoomPool instance = new ConnectionRoomPool();

	public ConnectionRoomPool() {
		this.add(TunnelRoom.class, 5);
		this.add(ChasmTunnelRoom.class, 3);
		this.add(RingConnectionRoom.class, 3);
		this.add(SpikedTunnelRoom.class, 2);
		this.add(BigRingConnectionRoom.class, 2);
		this.add(EmptyConnectionRoom.class, 3);
	}
}