package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.shop.ClassicShopRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.QuadShopRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.SpecialRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class ShopRoomPool extends Pool<SpecialRoom> {
	public static ShopRoomPool instance = new ShopRoomPool();

	public ShopRoomPool() {
		add(ClassicShopRoom.class, 1);
		add(QuadShopRoom.class, 1);
	}
}