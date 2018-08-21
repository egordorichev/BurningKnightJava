package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.special.BattleRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.ButtonPuzzleRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.SpecialRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.WeaponAltarRoom;
import org.rexcellentgames.burningknight.entity.pool.ClosingPool;

public class SpecialRoomPool extends ClosingPool<SpecialRoom> {
	public static SpecialRoomPool instance = new SpecialRoomPool();

	public SpecialRoomPool() {
		add(WeaponAltarRoom.class, 1f);
		add(BattleRoom.class, 1f);
		add(ButtonPuzzleRoom.class, 10000f);
	}
}