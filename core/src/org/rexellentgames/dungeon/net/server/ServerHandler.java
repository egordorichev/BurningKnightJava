package org.rexellentgames.dungeon.net.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.LoadState;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.PackageInfo;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
				this.sendToAll(Packets.makeSetEntityPosition(entity.getId(), entity.x, entity.y, entity.vel));
			}
		}
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
		}
	}

	private void playerConnected(Connection connection, Packets.PlayerConnected packet) {
		Log.info("User " + connection.getID() + " " + packet.name + " connected");
		Player player = new Player();

		player.setName(packet.name);
		player.connectionId = connection.getID();

		// Random pos on screen, todo: remove
		player.x = Random.newInt(0, Display.GAME_WIDTH - 16);
		player.y = Random.newInt(0, Display.GAME_HEIGHT - 16);

		connection.sendTCP(Packets.makeEntityAdded(Player.class, this.lastId, player.x, player.y, packet.name));
		this.sendAllTo(connection);

		this.onEntityAdded(player);
		this.players.put(connection.getID(), player);

		new Input(player.getId());
	}

	private void inputChange(Connection connection, Packets.InputChange packet) {
		Player player = this.players.get(connection.getID());
		Input.inputs.get(player.getId()).getKeys().put(packet.type, Input.State.values()[packet.state]);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);

		Log.info("User disconnected");
		int id = -1;

		Collection<Player> values = this.players.values();
		Player[] array = values.toArray(new Player[] {});

		for (int i = values.size() - 1; i >= 0; i--) {
			Player player = array[i];

			if (player.connectionId == connection.getID()) {
				this.players.remove(player);
				this.entities.remove(player);
				id = player.getId();

				break;
			}
		}

		if (id == -1) {
			Log.error("Removed entity is not found");
		} else {
			this.sendToAll(Packets.makeEntityRemoved(id));
		}
	}

	public void sendToAll(Object object) {
		for (Player player : this.players.values()) {
			this.server.getServer().sendToTCP(player.connectionId, object);
		}
	}

	public void sendAllTo(Connection connection) {
		for (NetworkedEntity entity : this.entities.values()) {
			connection.sendTCP(Packets.makeEntityAdded(entity.getClass(), entity.getId(), entity.x, entity.y, entity.getParam()));
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