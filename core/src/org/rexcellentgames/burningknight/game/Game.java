package org.rexcellentgames.burningknight.game;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.state.LoadState;
import org.rexcellentgames.burningknight.game.state.State;

public class Game {
	private State state;

	public Game() {

	}

	public void setState(State state) {
		if (!(this.state instanceof LoadState)) {
			Dungeon.ui.destroy();
			Dungeon.area.destroy();
		}

		State old = this.state;
		this.state = state;

		if (old != null) {
			old.destroy();
		}

		Ui.saveAlpha = 0;
		this.state.init();
		// Achievements.clear();
	}

	public void destroyState() {
		if (this.state != null) {
			State old = this.state;
			this.state = null; // For depth saving!
			old.destroy();
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

			if (!ui) {
				this.state.render();
			} else {
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