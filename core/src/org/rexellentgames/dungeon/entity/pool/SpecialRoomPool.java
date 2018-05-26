package org.rexellentgames.dungeon.entity.pool;

import org.rexellentgames.dungeon.entity.level.rooms.special.*;

public class SpecialRoomPool extends ClosingPool<SpecialRoom> {
	public static SpecialRoomPool instance = new SpecialRoomPool();

	public SpecialRoomPool() {
		add(TreasureRoom.class, 1f);
		add(WellRoom.class, 1f);
		add(ShopRoom.class, 1f);
		add(WeaponAltarRoom.class, 100f);
	}
}