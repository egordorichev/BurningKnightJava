package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.secret.SecretRoom;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class SecretRoomPool extends Pool<SecretRoom> {
	public static SecretRoomPool instance = new SecretRoomPool();

	public SecretRoomPool() {
		add(SecretRoom.class, 1f);
	}
}