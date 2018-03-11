package org.rexellentgames.dungeon.net.server;

import com.badlogic.gdx.ApplicationListener;
import com.esotericsoftware.kryonet.Server;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;

import java.io.IOException;

public class GameServer implements ApplicationListener {
	private Server server;
	private Dungeon dungeon;
	private ServerHandler serverHandler;
	private float t;

	public ServerHandler getServerHandler() {
		return this.serverHandler;
	}

	public void run() throws IOException {
		this.server = new Server();

		Packets.bind(this.server.getKryo());

		this.server.bind(Network.TCP_PORT, Network.UDP_PORT);
		this.server.addListener(this.serverHandler = new ServerHandler(this));

		this.server.start();
	}

	public void update(float dt) {
		this.t += dt;

		if (this.serverHandler != null) {
			this.serverHandler.update(dt);

			if (this.t % 0.05 <= 0.175f) {
				this.serverHandler.sendPackages(dt);
			}
		}
	}

	public void close() {
		this.server.close();
	}

	public Server getServer() {
		return this.server;
	}

	@Override
	public void create() {
		this.dungeon = new Dungeon();
		this.dungeon.create();
	}

	@Override
	public void resize(int width, int height) {
		this.dungeon.resize(width, height);
	}

	@Override
	public void render() {
		this.dungeon.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		this.dungeon.dispose();
		this.close();
	}
}