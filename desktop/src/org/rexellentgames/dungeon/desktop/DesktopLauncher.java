package org.rexellentgames.dungeon.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setWindowedMode(Display.GAME_WIDTH * 2, Display.GAME_HEIGHT * 2);
		config.setWindowSizeLimits(Display.GAME_WIDTH, Display.GAME_HEIGHT, 10000, 1000);

		new Lwjgl3Application(new Dungeon(), config);
	}
}
