package org.rexellentgames.dungeon.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.rexellentgames.dungeon.*;

public class DesktopLauncher {
	private static final int SCALE = 2;

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.addIcon("icon.png", Files.FileType.Internal);
		config.addIcon("icon32x32.png", Files.FileType.Internal);
		config.addIcon("icon128x128.png", Files.FileType.Internal);
		config.samples = 4;
		config.width = Display.GAME_WIDTH * SCALE;
		config.height = Display.GAME_HEIGHT * SCALE;
		config.title = "Burning Knight " + Version.asString();

		Dungeon.arg = arg;

		new LwjglApplication(new Client(), config);

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				Crash.report(thread, throwable);
				Gdx.app.exit();
			}
		});
	}
}