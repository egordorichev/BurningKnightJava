package org.rexellentgames.dungeon.net.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;

public class ClientHandler extends Listener {
	private GameClient client;

	public ClientHandler(GameClient client) {
		this.client = client;
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		Log.info("Got a package");

		if (object instanceof Packets.AddPlayer) {
			Packets.AddPlayer player = (Packets.AddPlayer) object;

			Log.info("Got player: " + player.x + ":" + player.y + ", " + player.id);
		}
	}

	public GameClient getClient() {
		return this.client;
	}
}