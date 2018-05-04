package org.rexellentgames.dungeon.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.rexellentgames.dungeon.*;
import org.rexellentgames.dungeon.util.DesktopSplashWorker;

public class DesktopLauncher {
	private static final int SCALE = 2;

	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("Burning Knight " + Version.asString());
		config.setWindowIcon("icon.png", "icon32x32.png", "icon128x128.png");
		config.setWindowedMode(Display.GAME_WIDTH * SCALE, Display.GAME_HEIGHT * SCALE);
		// config.samples = 4;

		Dungeon.arg = arg;
		Dungeon.worker = new DesktopSplashWorker();

		new Lwjgl3Application(new Client(), config);

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				Crash.report(thread, throwable);
				Gdx.app.exit();
			}
		});
	}
}