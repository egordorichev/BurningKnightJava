package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.boss.CircleBossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.CollumnsBossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.SimpleBossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class BossRoomPool extends Pool<EntranceRoom> {
	public static BossRoomPool instance = new BossRoomPool();

	public BossRoomPool() {
		add(SimpleBossRoom.class, 1f);
		add(CollumnsBossRoom.class, 1f);
		add(CircleBossRoom.class, 1f);
	}
}