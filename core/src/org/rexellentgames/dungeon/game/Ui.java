package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.levels.HubLevel;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.InGameState;
import org.rexellentgames.dungeon.util.Tween;

public class Ui {
	public static Ui ui;

	private TextureRegion cursor;
	private float scale = 1f;

	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui (cursor)");
	}

	public void update(float dt) {
		if (Input.instance.wasPressed("mouse0") || Input.instance.wasPressed("mouse1") || Input.instance.wasPressed("mouse2")) {
			Tween.to(new Tween.Task(1.2f, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}

				@Override
				public void onEnd() {
					super.onEnd();

					Tween.to(new Tween.Task(1f, 0.1f, Tween.Type.BACK_IN_OUT) {
						@Override
						public float getValue() {
							return scale;
						}

						@Override
						public void setValue(float value) {
							scale = value;
						}

						@Override
						public void onEnd() {
							super.onEnd();

						}
					});
				}
			});
		}
	}

	public void render() {
		if (Dungeon.game.getState() instanceof InGameState) {
			if (BurningKnight.instance != null) {
				// todo
			}

			if (Player.instance != null && Player.instance.isDead()) {
				Graphics.print("Game over!", Graphics.medium, 128);
				Graphics.print("Press space to restart", Graphics.medium, (float) (108 + Math.sin(Dungeon.time * 3) * 4));

				if (Input.instance.wasPressed("action")) {
					Dungeon.newGame();
				}
			}
		}

		// Cursor

		this.renderCursor();
	}

	public void renderCursor() {
		float s = (float) (1.2f + Math.cos(Dungeon.time / 1.5f) / 5f) * this.scale;
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Graphics.render(this.cursor, Input.instance.uiMouse.x,
			Input.instance.uiMouse.y,
			Dungeon.time * 60, 8, 8, false, false, s, s);
	}

	public void renderUi() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
	}
}