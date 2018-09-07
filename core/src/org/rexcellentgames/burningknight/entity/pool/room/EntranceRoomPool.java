package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.entrance.*;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class EntranceRoomPool extends Pool<EntranceRoom> {
	public static EntranceRoomPool instance = new EntranceRoomPool();

	public EntranceRoomPool() {
		add(EntranceRoom.class, 1f);
		// add(MazeEntranceRoom.class, 1f);
		add(CircleEntranceRoom.class, 1f);
		add(LineEntranceRoom.class, 1f);
		add(LineCircleRoom.class, 1f);
	}
}