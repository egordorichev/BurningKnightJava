package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.item.BrokeLineItemRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.item.ItemRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class ItemRoomPool extends Pool<ItemRoom> {
	public static ItemRoomPool instance = new ItemRoomPool();

	public ItemRoomPool() {
		add(BrokeLineItemRoom.class, 1f);
	}
}