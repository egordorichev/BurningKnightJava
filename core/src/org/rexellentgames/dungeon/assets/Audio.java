package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.util.Log;

import java.util.HashMap;

public class Audio {
	public static HashMap<String, Float> volumes = new HashMap<>();

	public static void targetAssets() {
		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(Gdx.files.internal("sfx/sfx.json"));

		for (JsonValue name : root) {
			Assets.manager.load("sfx/" + name.toString() + ".mp3", Sound.class);
			volumes.put(name.toString(), 1f);
		}

		root = reader.parse(Gdx.files.internal("music/music.json"));

		for (JsonValue name : root) {
			Assets.manager.load("music/" + name.toString() + ".mp3", Music.class);
		}

		FileHandle file = Gdx.files.external("sfx.json");

		if (file.exists()) {
			root = reader.parse(file);

			for (JsonValue name : root) {
				Log.info("Set " + name.name + " to " + name.asFloat());
				volumes.put(name.name, name.asFloat());
			}
		}
	}

	public static void loadAssets() {

	}

	public static long playSfx(String name, float volume, float pitch) {
		if (name.startsWith("menu") && !Settings.uisfx) {
			return 0;
		}

		Sound sound = getSound(name);

		long id = sound.play(volume * volumes.get(name) * Settings.sfx);
		sound.setPitch(id, pitch);

		return id;
	}

	public static Sound getSound(String sfx) {
		Sound sound = Assets.manager.get("sfx/" + sfx + ".mp3", Sound.class);

		if (sound == null) {
			Log.error("Sfx '" + sfx + "' is not found!");
		}

		return sound;
	}

	public static Music getMusic(String name) {
		Music music = Assets.manager.get("music/" + name + ".mp3", Music.class);

		if (music == null) {
			Log.error("Music '" + name + "' is not found!");
		}

		return music;
	}

	public static void destroy() {

	}
}