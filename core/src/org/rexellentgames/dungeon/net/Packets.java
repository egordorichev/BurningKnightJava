package org.rexellentgames.dungeon.net;

import com.esotericsoftware.kryo.Kryo;

public class Packets {
	public static class AddPlayer {
		public int id, x, y;
	}

	public static class GetClientId {
		public int id;
	}

	public static void bind(Kryo kryo) {
		kryo.register(GetClientId.class);
		kryo.register(AddPlayer.class);
	}
}