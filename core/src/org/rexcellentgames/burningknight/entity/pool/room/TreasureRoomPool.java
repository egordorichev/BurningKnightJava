package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.treasure.*;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class TreasureRoomPool extends Pool<TreasureRoom> {
	public static TreasureRoomPool instance = new TreasureRoomPool();

	public TreasureRoomPool() {
		add(CornerlessTreasureRoom.class, 1f);
		add(BrokeLineTreasureRoom.class, 1f);
		add(IslandTreasureRoom.class, 1f);
		add(CollumnTreasureRoom.class, 1f);
	}
}