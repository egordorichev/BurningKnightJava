package org.rexcellentgames.burningknight.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.HashMap;

public class Audio {
	public static HashMap<String, Float> volumes = new HashMap<>();
	private static Music current;
	private static boolean important;
	private static String last = "";

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

	public static void highPriority(String name) {
		Music music = getMusic(name);

		if (music == null) {
			Log.error("Music '" + name + "' is not found");
			return;
		}

		fadeOut();

		music.setLooping(false);
		music.setVolume(Settings.music);
		music.play();

		current = music;
		last = name;
		important = true;
	}

	public static void play(String name) {
		if (name == null || last.equals(name)) {
			return;
		}

		if (important) {
			if (!current.isPlaying()) {
				important = false;
			} else {
				return;
			}
		}

		final Music music = getMusic(name);

		if (music == null) {
			Log.error("Music '" + name + "' is not found");
			return;
		}

		music.setLooping(true);
		music.setVolume(0);
		music.play();

		Tween.to(new Tween.Task(Settings.music, 1f) {
			@Override
			public float getValue() {
				return music.getVolume();
			}

			@Override
			public void setValue(float value) {
				music.setVolume(value);
			}
		});

		fadeOut();

		current = music;
		last = name;
	}

	private static void fadeOut() {
		if (current != null) {
			final Music last = current;

			Tween.to(new Tween.Task(0, 1f) {
				@Override
				public float getValue() {
					return last.getVolume();
				}

				@Override
				public void setValue(float value) {
					last.setVolume(value);
				}
			});
		}
	}

	public static void update() {
		if (current != null) {
			current.setVolume(Settings.music);
		}
	}

	public static long playSfx(String name) {
		return playSfx(name, 1f, 1f);
	}

	public static long playSfx(String name, float volume) {
		return playSfx(name, volume, 1f);
	}
}