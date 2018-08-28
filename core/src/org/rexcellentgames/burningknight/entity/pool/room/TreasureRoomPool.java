package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.treasure.BrokeLineTreasureRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.treasure.CornerlessTreasureRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.treasure.IslandTreasureRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.treasure.TreasureRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class TreasureRoomPool extends Pool<TreasureRoom> {
	public static TreasureRoomPool instance = new TreasureRoomPool();

	public TreasureRoomPool() {
		add(CornerlessTreasureRoom.class, 1f);
		add(BrokeLineTreasureRoom.class, 1f);
		add(IslandTreasureRoom.class, 1f);
	}
}