package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.Dungeon;

public class Game {
	public static Game instance;

	private State state;

	public Game() {
		instance = this;
	}

	public void setState(State state) {
		this.destroyState();
		this.state = state;
		this.state.init();
	}

	public void destroyState() {
		if (this.state != null) {
			this.state.destroy();
		}
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
		this.destroyState();
	}
}