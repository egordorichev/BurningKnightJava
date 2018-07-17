package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.boss.SimpleBossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class BossRoomPool extends Pool<EntranceRoom> {
	public static BossRoomPool instance = new BossRoomPool();

	public BossRoomPool() {
		add(SimpleBossRoom.class, 1f);
	}
}