package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.ladder.CircleEntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.ladder.LineEntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.ladder.MazeEntranceRoom;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class EntranceRoomPool extends Pool<EntranceRoom> {
	public static EntranceRoomPool instance = new EntranceRoomPool();

	public EntranceRoomPool() {
		add(EntranceRoom.class, 1f);
		add(MazeEntranceRoom.class, 1f);
		add(CircleEntranceRoom.class, 1f);
		add(LineEntranceRoom.class, 1f);
	}
}