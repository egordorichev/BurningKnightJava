package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.secret.*;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class SecretRoomPool extends Pool<SecretRoom> {
	public static SecretRoomPool instance = new SecretRoomPool();

	public SecretRoomPool() {
		add(BombRoom.class, 1f);
		add(ChestRoom.class, 1f);
		add(GoldSecretRoom.class, 1f);
		add(HeartRoom.class, 1f);
	}
}