package org.rexellentgames.dungeon.net.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.NetworkedEntity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.HubState;
import org.rexellentgames.dungeon.game.state.LoginState;
import org.rexellentgames.dungeon.game.state.NetLoadState;
import org.rexellentgames.dungeon.net.PackageInfo;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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

			if (info == null) {
				Log.error("Empty package");
				return;
			}

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
		} else if (object instanceof Packets.SetEntityState) {
			this.setEntityState((Packets.SetEntityState) object);
		} else if (object instanceof Packets.ChatMessage) {
			UiLog.instance.print(((Packets.ChatMessage) object).message);
		}
	}

	public void send(Packets.Packet object) {
		final Object o = object;

		/*new Timer().schedule(new TimerTask() {
			@Override
			public void run() {*/
				client.getClient().sendTCP(o);
			/*}
		}, 100);*/
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
				Dungeon.longTime = 0;

				if (player.local) {
					Camera.instance.follow(entity);
				}
			}

			Dungeon.area.add(entity);
			this.entities.put(packet.id, entity);

			if (entity instanceof Creature) {
				((Creature) entity).tp(packet.x, packet.y);
			}
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
		Dungeon.area.remove(entity);
	}

	private void setEntityPosition(Packets.SetEntityPosition packet) {
		NetworkedEntity entity = this.entities.get(packet.id);

		if (entity == null) {
			Log.error("Updated entity is not found");
			return;
		}

		if (entity instanceof Creature) {
			Creature creature = (Creature) entity;

			if (!creature.local) {
				entity.vel.x = packet.vx;
				entity.vel.y = packet.vy;
			}

			if (creature.local && packet.t < creature.lastIndex) {
				if (compare(packet.x, creature.x) > 32f || compare(packet.y, creature.y) > 32f) {
					creature.tp(packet.x, packet.y);
				}

				return;
			}

			Creature.State state = creature.states.get(packet.t);

			if (state == null) {
				return;
			}

			creature.states.remove(packet.t);

			if (compare(packet.x, state.x) > 1f || compare(packet.y, state.y) > 1f) {
				creature.tp(packet.x, packet.y);
			} else {
				return;
			}
		} else {
			entity.x = packet.x;
			entity.y = packet.y;
		}

		entity.vel.x = packet.vx;
		entity.vel.y = packet.vy;
	}

	private static float compare(float a, float b) {
		return Math.max(a, b) - Math.min(a, b);
	}

	private void setEntityState(Packets.SetEntityState packet) {
		NetworkedEntity entity = this.entities.get(packet.id);

		if (entity == null) {
			Log.error("Updated entity is not found");
			return;
		}

		entity.become(packet.state);
	}

	public GameClient getClient() {
		return this.client;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}
}