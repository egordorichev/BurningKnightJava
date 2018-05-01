package org.rexellentgames.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import org.rexellentgames.dungeon.util.Log;

import java.io.FileWriter;
import java.io.IOException;

public class Settings {
	public static boolean fullscreen;
	public static boolean vsync = true;
	public static boolean blood = true;
	public static boolean gore = true;
	public static boolean shaders = false;
	public static boolean uisfx = true;
	public static int quality = 2;
	public static float screenshake = 0.7f;
	public static float music = 0.7f;
	public static float sfx = 0.7f;

	public static void load() {
		Log.info("Loading settings...");

		FileHandle handle = Gdx.files.external(".bk/settings.json");

		if (handle.exists()) {
			JsonReader reader = new JsonReader();
			JsonValue root = reader.parse(handle);

			fullscreen = root.getBoolean("fullscreen");
			blood = root.getBoolean("blood");
			uisfx = root.getBoolean("uisfx");
			gore = root.getBoolean("gore");
			shaders = root.getBoolean("shaders");
			vsync = root.getBoolean("vsync");
			quality = root.getInt("quality");
			screenshake = root.getFloat("screenshake");
			sfx = root.getFloat("sfx");
			music = root.getFloat("music");
		}
	}

	public static void save() {
		Log.info("Saving settings...");

		try {
			FileWriter w = new FileWriter(Gdx.files.external(".bk/settings.json").file().getAbsolutePath());
			JsonWriter writer = new JsonWriter(w);

			writer.write("{ \"fullscreen\" : " + fullscreen + ", \"blood\" : " + blood + ", \"gore\" : " + gore +
				", \"shaders\" : " + shaders + ", \"vsync\" : " + vsync + ", \"quality\" : " + quality + ", \"screenshake\" : " + ((double) screenshake) +
				", \"sfx\" : " + sfx + ", \"music\" : " + music + ", \"uisfx\" : " + uisfx + " }");

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}