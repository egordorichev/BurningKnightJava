package org.rexellentgames.dungeon.net;

import org.rexellentgames.dungeon.net.client.GameClient;
import org.rexellentgames.dungeon.net.server.GameServer;
import org.rexellentgames.dungeon.util.Log;

import java.io.IOException;

public class Network {
	public static boolean SERVER = false;

	public static final String IP = "localhost";
	public static final int TCP_PORT = 3333;
	public static final int UDP_PORT = TCP_PORT + 1;

	private static org.rexellentgames.dungeon.net.client.GameClient client;
	private static org.rexellentgames.dungeon.net.server.GameServer server;

	public static void run() {
		if (SERVER) {
			Log.info("Starting server...");
			server = new GameServer();

			try {
				server.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.info("Starting client...");
			client = new GameClient();

			try {
				client.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}