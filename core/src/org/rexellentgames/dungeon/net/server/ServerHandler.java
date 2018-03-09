package org.rexellentgames.dungeon.net.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.net.server.GameServer;
import org.rexellentgames.dungeon.util.Log;

public class ServerHandler extends Listener {
	private org.rexellentgames.dungeon.net.server.GameServer server;

	public ServerHandler(GameServer server) {
		this.server = server;
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);

		Log.info("Got a package");

		if (object instanceof Packets.AddPlayer) {
			Packets.AddPlayer player = (Packets.AddPlayer) object;

			Log.info("Got player: " + player.x + ":" + player.y + ", " + player.id);
		} else if (object instanceof Packets.GetClientId) {
			Packets.AddPlayer player = new Packets.AddPlayer();

			player.x = 10;
			player.y = 10;
			player.id = connection.getID();

			this.server.getServer().sendToTCP(connection.getID(), player);
		}
	}
}