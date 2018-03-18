package org.rexellentgames.dungeon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.rexellentgames.dungeon.Client;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Version;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.client.GameClient;
import org.rexellentgames.dungeon.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DesktopLauncher {
	private static final int SCALE = 2;

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Display.GAME_WIDTH * SCALE;
		config.height = Display.GAME_HEIGHT * SCALE;
		config.title = "Burning Knight " + Version.asString();

		new LwjglApplication(new Client(), config);

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				throwable.printStackTrace();
			}
		});
	}
}