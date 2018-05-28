package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.secret.BombRoom;
import org.rexellentgames.dungeon.entity.level.rooms.secret.ChestRoom;
import org.rexellentgames.dungeon.entity.level.rooms.secret.GoldSecretRoom;
import org.rexellentgames.dungeon.entity.level.rooms.secret.SecretRoom;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class SecretRoomPool extends Pool<SecretRoom> {
	public static SecretRoomPool instance = new SecretRoomPool();

	public SecretRoomPool() {
		add(BombRoom.class, 1f);
		add(ChestRoom.class, 1f);
		add(GoldSecretRoom.class, 100f);
	}
}