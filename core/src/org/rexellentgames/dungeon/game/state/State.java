package org.rexellentgames.dungeon.game.state;

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
}