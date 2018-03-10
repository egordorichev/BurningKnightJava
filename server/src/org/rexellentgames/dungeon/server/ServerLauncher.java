package org.rexellentgames.dungeon.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.server.GameServer;
import org.rexellentgames.dungeon.util.Log;
import org.mockito.Mockito;

import java.io.IOException;

public class ServerLauncher extends ApplicationAdapter {
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
      }
    });

		Network.SERVER = true;
		Log.info("Starting the server...");

		try {
			Network.server = new GameServer();
			Gdx.gl = Mockito.mock(GL20.class);

			new HeadlessApplication(Network.server);
			Network.server.run();
		} catch (IOException e) {
			Log.error("Error: port already in use!");
		}
	}
}