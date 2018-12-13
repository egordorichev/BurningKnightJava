package org.rexcellentgames.burningknight.game;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.state.InventoryState;
import org.rexcellentgames.burningknight.game.state.LoadState;
import org.rexcellentgames.burningknight.game.state.State;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Log;

public class Game {
	private State state;

	public Game() {

	}

	private State toSet;

	public void setState(State state) {
		toSet = state;
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
		if (toSet != null) {
			if (this.state != null) {
				if (!(this.state instanceof LoadState || state instanceof InventoryState)) {
					Dungeon.ui.destroy();
					Dungeon.area.destroy();
				}

				if (state instanceof InventoryState) {
					Dungeon.ui.destroy();
				}
			}

			State old = this.state;
			Camera.instance.resetShake();
			Log.info("Set state to " + toSet.getClass().getSimpleName());
			this.state = toSet;
			toSet = null;
			Dialog.active = null;

			if (old != null) {
				old.destroy();
			}

			Dungeon.blood = 0;
			this.state.init();
		}

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