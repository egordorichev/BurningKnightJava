
package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.shop.*;
import org.rexcellentgames.burningknight.entity.level.rooms.special.SpecialRoom;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class ShopRoomPool extends Pool<SpecialRoom> {
	public static ShopRoomPool instance = new ShopRoomPool();

	public ShopRoomPool() {
		add(ClassicShopRoom.class, 1);
		add(QuadShopRoom.class, 1);
		add(GoldShopRoom.class, 1);
		//add(BigShop.class, 0.1f);
		//add(GiantShop.class, 0.03f);
		//add(TinyShop.class, 0.4f);
		//add(DoubleShop.class, 0.5f);
	}
}