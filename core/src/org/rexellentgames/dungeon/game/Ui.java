package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.boss.Boss;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.InGameState;
import org.rexellentgames.dungeon.util.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Ui {
	public static Ui ui;
	private float scale = 1f;
	public HashMap<Class<? extends Boss>, Healthbar> healthbars = new HashMap<>();

	private TextureRegion cursor;
	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui (cursor)");
	}

	public void update(float dt) {
		for (Boss boss : Boss.all) {
			if (!healthbars.containsKey(boss.getClass())) {
				Healthbar healthbar = new Healthbar();
				healthbar.boss = boss;

				healthbars.put(boss.getClass(), healthbar);
			}
		}

		Healthbar[] bars = healthbars.values().toArray(new Healthbar[] {});

		for (int i = bars.length - 1; i >= 0; i--) {
			bars[i].update(dt);

			if (bars[i].done) {
				healthbars.remove(bars[i].boss.getClass());
			}
		}

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
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (Dungeon.game.getState() instanceof InGameState) {
			if (Player.instance != null && Player.instance.isDead()) {
				Graphics.print("Game over!", Graphics.medium, 128);
				Graphics.print("Press space to restart", Graphics.medium, (float) (108 + Math.sin(Dungeon.time * 3) * 4));

				if (Input.instance.wasPressed("action")) {
					Dungeon.newGame();
				}
			}
		}

		for (Healthbar healthbar : this.healthbars.values()) {
			healthbar.render();
		}
	}

	public void renderCursor() {
		float s = (float) (1.2f + Math.cos(Dungeon.time / 1.5f) / 5f) * this.scale;
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		//float dx = Math.abs(Input.instance.target.x - Input.instance.mouse.x);
		//float dy = Math.abs(Input.instance.target.y - Input.instance.mouse.y);

		float a = Dungeon.time * 60;

		float sx = 1; //MathUtils.clamp(1f, 2f, dx / 30);
		float sy = 1; //MathUtils.clamp(1f, 2f, dy / 30);

		Graphics.render(this.cursor, Input.instance.uiMouse.x,
			Input.instance.uiMouse.y, a, 8, 8, false, false, sx * s, sy * s);
	}

	public void renderUi() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
	}
}