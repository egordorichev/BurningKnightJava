package org.rexellentgames.dungeon.net.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.LoadState;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.PackageInfo;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

import java.util.*;

public class ServerHandler extends Listener {
	private GameServer server;
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private HashMap<Integer, NetworkedEntity> entities = new HashMap<Integer, NetworkedEntity>();
	private int lastId;
	private ArrayList<PackageInfo> packages = new ArrayList<PackageInfo>();

	public ServerHandler(GameServer server) {
		this.server = server;
	}

	public void update(float dt) {
		while (this.packages.size() > 0) {
			PackageInfo info = this.packages.remove(0);
			this.processPackage(info.connection, info.object);
		}
	}

	public void sendPackages(float dt) {
		for (NetworkedEntity entity : this.entities.values()) {
			if (!entity.isSleeping()) {
				long t = 0;

				if (entity instanceof Creature) {
					t = ((Creature) entity).lastIndex;
				}

				this.sendToAll(Packets.makeSetEntityPosition(entity.getId(), entity.x, entity.y, entity.vel, t));
			}
		}
	}

	public void send(int id, Object object) {
		this.server.getServer().sendToTCP(id, object);
	}

	public void send(Connection connection, Object object) {
		connection.sendTCP(object);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		this.packages.add(new PackageInfo(object, connection));
	}

	private void processPackage(Connection connection, Object object) {
		if (object instanceof Packets.PlayerConnected) {
			this.playerConnected(connection, (Packets.PlayerConnected) object);
		} else if (object instanceof Packets.InputChange) {
			this.inputChange(connection, (Packets.InputChange) object);
		} else if (object instanceof Packets.PlayerDisconnected) {
			this.playerDisconnected(connection);
		} else if (object instanceof Packets.ChatMessage) {
			this.sendToAllExcept(object, connection);
		}
	}

	private void playerConnected(Connection connection, Packets.PlayerConnected packet) {
		Log.info("User " + connection.getID() + " " + packet.name + " connected");
		Player player = new Player();

		player.x = 0;
		player.y = 0;

		player.setName(packet.name);
		player.connectionId = connection.getID();

		this.send(connection, Packets.makeEntityAdded(Player.class, this.lastId, player.x, player.y, packet.name));
		this.sendAllTo(connection);

		this.onEntityAdded(player);
		this.players.put(connection.getID(), player);

		new Input(player.getId());

		this.send(connection, Packets.makeChatMessage("[green]Welcome to the hub! Type esc for chat"));
		this.sendToAllExcept(Packets.makeChatMessage("[green]" + packet.name + " connected!"), connection);

		Dungeon.longTime = 0;
	}

	private void playerDisconnected(Connection connection) {
		Log.info("User disconnected");
		int id = -1;
		String name = "";

		Collection<Player> values = this.players.values();
		Player[] array = values.toArray(new Player[] {});

		for (int i = values.size() - 1; i >= 0; i--) {
			Player player = array[i];

			if (player.connectionId == connection.getID()) {
				this.players.remove(connection.getID());
				this.entities.remove(player.getId());
				id = player.getId();
				name = player.getName();

				break;
			}
		}

		if (id == -1) {
			Log.error("Removed entity is not found");
		} else {
			this.sendToAll(Packets.makeEntityRemoved(id));
			this.sendToAllExcept(Packets.makeChatMessage("[orange]" + name + " disconnected!"), connection);
		}
	}

	private void inputChange(Connection connection, Packets.InputChange packet) {
		Player player = this.players.get(connection.getID());
		player.lastIndex = packet.t;

		Input.inputs.get(player.getId()).getKeys().put(packet.type, Input.State.values()[packet.state]);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		this.packages.add(new PackageInfo(new Packets.PlayerDisconnected(), connection));
	}

	public void sendToAll(Object object) {
		for (Player player : this.players.values()) {
			this.send(player.connectionId, object);
		}
	}

	public void sendToAllExcept(Object object, Connection connection) {
		for (Player player : this.players.values()) {
			if (player.connectionId != connection.getID()) {
				this.send(player.connectionId, object);
			}
		}
	}

	public void sendAllTo(Connection connection) {
		for (NetworkedEntity entity : this.entities.values()) {
			this.send(connection, Packets.makeEntityAdded(entity.getClass(), entity.getId(), entity.x, entity.y, entity.getParam()));
		}

		// todo: send the map
	}

	private void onEntityAdded(NetworkedEntity entity) {
		entity.setId(this.lastId);

		this.lastId++;
		this.entities.put(entity.getId(), entity);
		Dungeon.area.add(entity);

		this.sendToAll(Packets.makeEntityAdded(Player.class, entity.getId(), entity.x, entity.y, entity.getParam()));
	}
}