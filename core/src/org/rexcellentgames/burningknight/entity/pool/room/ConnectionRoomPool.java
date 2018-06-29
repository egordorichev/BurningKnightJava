package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.connection.ChasmTunnelRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.RingConnectionRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.TunnelRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.CircleRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.*;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.CircleRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;

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