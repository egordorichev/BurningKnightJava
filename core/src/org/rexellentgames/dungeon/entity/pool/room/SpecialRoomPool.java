package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.special.*;
import org.rexellentgames.dungeon.entity.pool.ClosingPool;

public class SpecialRoomPool extends ClosingPool<SpecialRoom> {
	public static SpecialRoomPool instance = new SpecialRoomPool();

	public SpecialRoomPool() {
		add(TreasureRoom.class, 1f);
		add(WellRoom.class, 1f);
		add(ShopRoom.class, 1f);
		add(WeaponAltarRoom.class, 100f);
	}
}