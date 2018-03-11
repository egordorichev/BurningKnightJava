package org.rexellentgames.dungeon.net.client;

import com.esotericsoftware.kryonet.Client;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.state.HubState;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.net.client.ClientHandler;
import org.rexellentgames.dungeon.util.Random;

import java.io.IOException;

public class GameClient {
	private Client client;
	private ClientHandler clientHandler;

	public ClientHandler getClientHandler() {
		return this.clientHandler;
	}

	public void update(float dt) {
		if (this.clientHandler != null) {
			this.clientHandler.update(dt);
		}
	}

	public void run(String name) throws IOException {
		this.client = new Client();

		Packets.bind(this.client.getKryo());

		this.client.start();
		this.client.addListener(this.clientHandler = new ClientHandler(this));
		this.client.connect(5000, Network.IP, Network.TCP_PORT, Network.UDP_PORT);

		this.client.sendTCP(Packets.makePlayerConnected(Player.NAME));

		Dungeon.game.setState(new HubState());
	}

	public Client getClient() {
		return this.client;
	}
}