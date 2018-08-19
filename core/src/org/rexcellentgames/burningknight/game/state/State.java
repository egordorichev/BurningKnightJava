package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.util.Tween;

public class State {
	private boolean paused;

	public void onPause() {

	}

	public void onUnpause() {

	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;

		if (this.paused) {
			this.onPause();
		} else {
			this.onUnpause();
		}
	}

	public State() {

	}

	public void init() {

	}

	public void destroy() {

	}

	public void update(float dt) {

	}

	public void render() {

	}

	public void renderUi() {

	}

	public void resize(int width, int height) {

	}

	public static void transition(final Runnable runnable) {
		Tween.to(new Tween.Task(0, 0.2f) {
			@Override
			public float getValue() {
				return Dungeon.dark;
			}

			@Override
			public void setValue(float value) {
				Dungeon.dark = value;
			}

			@Override
			public void onEnd() {
				runnable.run();

				Tween.to(new Tween.Task(1, 0.2f) {
					@Override
					public float getValue() {
						return Dungeon.dark;
					}

					@Override
					public void setValue(float value) {
						Dungeon.dark = value;
					}
				});
			}

			@Override
			public boolean runWhenPaused() {
				return true;
			}
		});
	}
}