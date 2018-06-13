package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
	public static AssetManager manager;
	private static boolean done;

	public static void init() {
		manager = new AssetManager();

		Audio.targetAssets();
		Graphics.targetAssets();
	}

	public static boolean updateLoading() {
		boolean val = manager.update();

		if (val && !done) {
			done = true;
			loadAssets();
		}

		return val;
	}

	public static void finishLoading() {
		manager.finishLoading();
		loadAssets();
	}

	public static void loadAssets() {
		Audio.loadAssets();
		Graphics.loadAssets();
	}

	public static void destroy() {
		Graphics.destroy();
		Audio.destroy();

		if (manager != null) {
			manager.dispose();
		}
	}
}