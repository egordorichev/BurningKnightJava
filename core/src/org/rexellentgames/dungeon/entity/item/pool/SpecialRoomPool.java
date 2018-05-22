package org.rexellentgames.dungeon.entity.item.pool;

import org.rexellentgames.dungeon.entity.level.rooms.special.ShopRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.SpecialRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.TreasureRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.WellRoom;

public class SpecialRoomPool extends ClosingPool<SpecialRoom> {
	public static SpecialRoomPool instance = new SpecialRoomPool();

	public SpecialRoomPool() {
		add(TreasureRoom.class, 1f);
		add(WellRoom.class, 1f);
		add(ShopRoom.class, 1f);
	}
}