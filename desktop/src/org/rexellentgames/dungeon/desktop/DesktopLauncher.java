package org.rexellentgames.dungeon.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import org.rexellentgames.dungeon.*;
import org.rexellentgames.dungeon.util.DesktopSplashWorker;
import org.rexellentgames.dungeon.util.Random;

public class DesktopLauncher {
	private static final int SCALE = 2;

	public static void main(String[] arg) {
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			Crash.report(thread, throwable);
			Gdx.app.exit();
		});

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setWindowListener(new Lwjgl3WindowListener() {
			@Override
			public void created(Lwjgl3Window window) {

			}

			@Override
			public void iconified(boolean isIconified) {

			}

			@Override
			public void maximized(boolean isMaximized) {

			}

			@Override
			public void focusLost() {
				Dungeon.instance.pause();
			}

			@Override
			public void focusGained() {
				Dungeon.instance.resume();
			}

			@Override
			public boolean closeRequested() {
				return true;
			}

			@Override
			public void filesDropped(String[] files) {

			}

			@Override
			public void refreshRequested() {

			}
		});

		config.setTitle("Burning Knight " + Version.asString() + ": " + titles[Random.newInt(titles.length)]);
		config.setWindowIcon("icon.png", "icon32x32.png", "icon128x128.png");
		config.setWindowedMode(Display.GAME_WIDTH * SCALE, Display.GAME_HEIGHT * SCALE);
		config.setIdleFPS(0);
		config.setBackBufferConfig(1, 1, 1, 1, 0, 0, 4);

		Dungeon.arg = arg;
		Dungeon.worker = new DesktopSplashWorker();

		new Lwjgl3Application(new Client(), config);
	}

	private static String[] titles = new String[] {
		"Fireproof",
		"Might burn",
		"'Friendly' fire",
		"Get ready to burn",
		"Do you need some heat?",
		"BBQ is ready!"
	};
}