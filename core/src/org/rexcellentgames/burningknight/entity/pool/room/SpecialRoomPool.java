package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.special.*;
import org.rexcellentgames.burningknight.entity.pool.ClosingPool;

public class SpecialRoomPool extends ClosingPool<SpecialRoom> {
	public static SpecialRoomPool instance = new SpecialRoomPool();

	public SpecialRoomPool() {
		add(WeaponAltarRoom.class, 1f);
		add(BattleRoom.class, 1f);
		add(ButtonPuzzleRoom.class, 1f);
		add(LeverPuzzleRoom.class, 1f);
	}
}