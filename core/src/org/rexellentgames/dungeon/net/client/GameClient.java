package org.rexellentgames.dungeon.net.client;

import com.esotericsoftware.kryonet.Client;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.net.client.ClientHandler;

import java.io.IOException;

public class GameClient {
	private Client client;

	public void run() throws IOException {
		this.client = new Client();

		Packets.bind(this.client.getKryo());

		this.client.start();
		this.client.addListener(new ClientHandler(this));
		this.client.connect(5000, Network.IP, Network.TCP_PORT, Network.UDP_PORT);
	}

	public Client getClient() {
		return this.client;
	}
}