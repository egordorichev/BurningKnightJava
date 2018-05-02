package org.rexellentgames.dungeon.util;

import java.awt.*;

public class DesktopSplashWorker implements SplashWorker {
	@Override
	public void closeSplashScreen() {
		SplashScreen screen = SplashScreen.getSplashScreen();

		if (screen != null && screen.isVisible()) {
			screen.close();
		}
	}
}