package org.rexellentgames.dungeon.net.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.state.HubState;
import org.rexellentgames.dungeon.game.state.LoginState;
import org.rexellentgames.dungeon.game.state.NetLoadState;
import org.rexellentgames.dungeon.net.PackageInfo;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientHandler extends Listener {
	private GameClient client;
	public static ClientHandler instance;

	private ArrayList<Player> players = new ArrayList<Player>();
	private HashMap<Integer, NetworkedEntity> entities = new HashMap<Integer, NetworkedEntity>();
	private ArrayList<PackageInfo> packages = new ArrayList<PackageInfo>();

	public ClientHandler(GameClient client) {
		this.client = client;
		instance = this;
	}

	public void update(float dt) {
		while (this.packages.size() > 0) {
			PackageInfo info = this.packages.remove(0);
			this.processPackage(info.connection, info.object);
		}
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		this.packages.add(new PackageInfo(object, connection));
	}

	private void processPackage(Connection connection, Object object) {
		if (object instanceof Packets.EntityAdded) {
			this.entityAdded((Packets.EntityAdded) object);
		} else if (object instanceof Packets.EntityRemoved) {
			this.entityRemoved((Packets.EntityRemoved) object);
		} else if (object instanceof Packets.SetEntityPosition) {
			this.setEntityPosition((Packets.SetEntityPosition) object);
		}
	}

	private void entityAdded(Packets.EntityAdded packet) {
		try {
			NetworkedEntity entity = (NetworkedEntity) Class.forName(packet.clazz).newInstance();

			entity.x = packet.x;
			entity.y = packet.y;
			entity.setId(packet.id);

			if (entity instanceof Player) {
				Log.info("Player " + packet.id + " " + packet.param + " joined");

				Player player = (Player) entity;
				player.setName(packet.param);

				this.players.add(player);
			}

			Dungeon.area.add(entity);
			this.entities.put(packet.id, entity);
		} catch (Exception e) {
			Log.error("Failed to create an entity!");
			e.printStackTrace();
		}
	}

	private void entityRemoved(Packets.EntityRemoved packet) {
		NetworkedEntity entity = this.entities.get(packet.id);

		if (entity == null) {
			Log.error("Removed entity is not found");
			return;
		}

		if (entity instanceof Player) {
			this.players.remove(entity);
			Log.info("Player disconnected, " + this.players.size() + " players left");
		}

		this.entities.remove(packet.id);
	}

	private void setEntityPosition(Packets.SetEntityPosition packet) {
		NetworkedEntity entity = this.entities.get(packet.id);

		if (entity == null) {
			return;
		}

		if (entity instanceof Creature) {
			((Creature) entity).tp(packet.x, packet.y);
		} else {
			entity.x = packet.x;
			entity.y = packet.y;
		}
	}

	public GameClient getClient() {
		return this.client;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}
}