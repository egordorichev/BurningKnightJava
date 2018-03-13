package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.state.LoadState;
import org.rexellentgames.dungeon.game.state.State;

public class Game {
	public static Game instance;

	private org.rexellentgames.dungeon.game.state.State state;

	public Game() {
		instance = this;
	}

	public void setState(org.rexellentgames.dungeon.game.state.State state) {
		if (!(this.state instanceof LoadState)) {
			Dungeon.ui.destroy();
			Dungeon.area.destroy();
		}

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
			Graphics.shape.setProjectionMatrix(Camera.instance.getCamera().combined);
			this.state.render();
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);
			this.state.renderUi();
		}
	}

	public void destroy() {
		this.destroyState();
	}
}