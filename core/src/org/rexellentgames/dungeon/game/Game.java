package org.rexellentgames.dungeon.game;

public class Game {
	public static Game instance;

	private State state;

	public Game() {
		instance = this;
	}

	public void setState(State state) {
		if (this.state != null) {
			this.state.destroy();
		}

		this.state = state;
		this.state.init();
	}

	public State getState() {
		return this.state;
	}

	public void update(float dt) {
		if (this.state != null) {
			this.state.update(dt);
		}
	}

	public void render() {
		if (this.state != null) {
			this.state.render();
		}
	}

	public void destroy() {
		if (this.state != null) {
			this.state.destroy();
		}
	}
}