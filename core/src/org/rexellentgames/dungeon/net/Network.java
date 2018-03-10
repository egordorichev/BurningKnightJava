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

	public static GameClient client;
	public static GameServer server;
}