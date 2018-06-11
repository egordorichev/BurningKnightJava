package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.audio.Music;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

public class MusicManager {
	private static Music current;
	private static boolean important;
	private static String last = "";

	public static void highPriority(String name) {
		Music music = Graphics.getMusic(name);

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

		Music music = Graphics.getMusic(name);

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
}