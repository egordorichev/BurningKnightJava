package org.rexellentgames.dungeon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Version;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Log;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Display.GAME_WIDTH * 2;
		config.height = Display.GAME_HEIGHT * 2;
		config.title = "Burning Knight " + Version.asString();

		new LwjglApplication(new Dungeon(), config);
	}
}