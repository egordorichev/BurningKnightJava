package org.rexellentgames.dungeon.net;

import com.esotericsoftware.kryo.Kryo;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Packets {
	public static class Packet {

	}

	public static class PlayerConnected extends Packet {
		public String name;
	}

	public static PlayerConnected makePlayerConnected(String name) {
		PlayerConnected packet = new PlayerConnected();
		packet.name = name;

		return packet;
	}

	public static class PlayerDisconnected extends Packet {

	}

	public static class SetGameState extends Packet {
		public String name;
	}

	public static SetGameState makeSetGameState(String name) {
		SetGameState packet = new SetGameState();
		packet.name = name;

		return packet;
	}

	public static class InputChange extends Packet {
		public byte state;
		public String type;
		public byte data;
		public long t;
	}

	public static InputChange makeInputChanged(Input.State state, String type, int data) {
		InputChange packet = new InputChange();

		packet.state = (byte) state.getValue();
		packet.type = type;
		packet.data = (byte) data;
		packet.t = Dungeon.longTime;

		return packet;
	}

	public static class EntityAdded extends Packet {
		public String clazz;
		public float x;
		public float y;
		public int id;
		public String param;
	}

	public static EntityAdded makeEntityAdded(Class<? extends NetworkedEntity> clazz, int id, float x, float y, String param) {
		EntityAdded packet = new EntityAdded();

		packet.clazz = clazz.getName();
		packet.x = x;
		packet.y = y;
		packet.id = id;
		packet.param = param;

		return packet;
	}

	public static class EntityRemoved extends Packet {
		public int id;
	}

	public static EntityRemoved makeEntityRemoved(int id) {
		EntityRemoved packet = new EntityRemoved();
		packet.id = id;

		return packet;
	}

	public static class SetEntityPosition extends Packet {
		public int id;
		public float x;
		public float y;
		public float vx;
		public float vy;
		public long t;
	}

	public static SetEntityPosition makeSetEntityPosition(int id, float x, float y, Point vel, long t) {
		SetEntityPosition packet = new SetEntityPosition();

		packet.x = x;
		packet.y = y;
		packet.id = id;
		packet.vx = vel.x;
		packet.vy = vel.y;
		packet.t = t;

		return packet;
	}

	public static class SetEntityState extends Packet {
		public int id;
		public String state;
	}

	public static SetEntityState makeSetEntityState(int id, String state) {
		SetEntityState packet = new SetEntityState();

		packet.id = id;
		packet.state = state;

		return packet;
	}

	public static class ChatMessage extends Packet {
		public String message;
	}

	public static ChatMessage makeChatMessage(String message) {
		ChatMessage packet = new ChatMessage();
		packet.message = message;

		return packet;
	}

	public static class Level extends Packet {
		public int depth;
		public byte[] data;
		public byte[] variants;
		public int w;
		public int h;
	}

	public static Level makeLevel(byte[] data, byte[] variants, int depth, int w, int h) {
		Level packet = new Level();

		packet.data = data;
		packet.variants = variants;
		packet.depth = depth;
		packet.w = w;
		packet.h = h;

		return packet;
	}

	public static class TpEntity {
		public float x;
		public float y;
		public int id;
	}

	public static TpEntity makeTpEntity(int id, float x, float y) {
		TpEntity packet = new TpEntity();

		packet.x = x;
		packet.y = y;
		packet.id = id;

		return packet;
	}

	public static void bind(Kryo kryo) {
		kryo.register(byte[].class);
		kryo.register(PlayerConnected.class);
		kryo.register(SetGameState.class);
		kryo.register(InputChange.class);
		kryo.register(EntityAdded.class);
		kryo.register(EntityRemoved.class);
		kryo.register(SetEntityPosition.class);
		kryo.register(SetEntityState.class);
		kryo.register(ChatMessage.class);
		kryo.register(Level.class);
		kryo.register(TpEntity.class);
	}
}