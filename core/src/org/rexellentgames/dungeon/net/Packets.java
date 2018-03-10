package org.rexellentgames.dungeon.net;

import com.esotericsoftware.kryo.Kryo;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Packets {
	public static class PlayerConnected {
		public String name;
	}

	public static PlayerConnected makePlayerConnected(String name) {
		PlayerConnected packet = new PlayerConnected();
		packet.name = name;

		return packet;
	}

	public static class SetGameState {
		public String name;
	}

	public static SetGameState makeSetGameState(String name) {
		SetGameState packet = new SetGameState();
		packet.name = name;

		return packet;
	}

	public static class InputChange {
		public byte state;
		public String type;
		public byte data;
	}

	public static InputChange makeInputChanged(Input.State state, String type, int data) {
		InputChange packet = new InputChange();

		packet.state = (byte) state.getValue();
		packet.type = type;
		packet.data = (byte) data;

		return packet;
	}

	public static class EntityAdded {
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

	public static class EntityRemoved {
		public int id;
	}

	public static EntityRemoved makeEntityRemoved(int id) {
		EntityRemoved packet = new EntityRemoved();
		packet.id = id;

		return packet;
	}

	public static class SetEntityPosition {
		public int id;
		public float x;
		public float y;
		public float vx;
		public float vy;
	}

	public static SetEntityPosition makeSetEntityPosition(int id, float x, float y, Point vel) {
		SetEntityPosition packet = new SetEntityPosition();

		packet.x = x;
		packet.y = y;
		packet.id = id;
		packet.vx = vel.x;
		packet.vy = vel.y;

		return packet;
	}

	public static class SetEntityState {
		public int id;
		public String state;
	}

	public static SetEntityState makeSetEntityState(int id, String state) {
		SetEntityState packet = new SetEntityState();

		packet.id = id;
		packet.state = state;

		return packet;
	}

	public static void bind(Kryo kryo) {
		kryo.register(PlayerConnected.class);
		kryo.register(SetGameState.class);
		kryo.register(InputChange.class);
		kryo.register(EntityAdded.class);
		kryo.register(EntityRemoved.class);
		kryo.register(SetEntityPosition.class);
		kryo.register(SetEntityState.class);
	}
}