package org.rexellentgames.dungeon.assets;

import com.badlogic.gdx.audio.Music;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.util.Tween;

public class MusicManager {
	private static Music current;

	public static void play(String name) {
		Music music = Graphics.getMusic(name);

		if (music == null) {
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

		current = music;
	}

	public static void update() {
		if (current != null) {
			current.setVolume(Settings.music);
		}
	}
}