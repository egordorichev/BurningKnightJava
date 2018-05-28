package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.ladder.*;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class EntranceRoomPool extends Pool<EntranceRoom> {
	public static EntranceRoomPool instance = new EntranceRoomPool();

	public EntranceRoomPool() {
		add(EntranceRoom.class, 1f);
		add(MazeEntranceRoom.class, 1f);
		add(CircleEntranceRoom.class, 1f);
		add(LineEntranceRoom.class, 1f);
		add(LineCircleRoom.class, 1f);
	}
}