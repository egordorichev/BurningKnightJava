package org.rexellentgames.dungeon.game;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.state.LoadState;
import org.rexellentgames.dungeon.game.state.State;

public class Game {
	private org.rexellentgames.dungeon.game.state.State state;

	public Game() {
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
		render(true);
	}

	public void render(boolean ui) {
		if (this.state != null) {
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			this.state.render();

			if (ui) {
				renderUi();
			}
		}
	}

	public void renderUi() {
		if (this.state != null) {
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);
			this.state.renderUi();
		}
	}

	public void destroy() {
		this.destroyState();
	}
}