package org.rexcellentgames.burningknight.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.HashMap;

public class Ui {
	public static Ui ui;
	private float scale = 1f;
	public HashMap<Class<? extends Boss>, Healthbar> healthbars = new HashMap<>();

	private TextureRegion cursor;
	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui-cursor");
	}

	public void update(float dt) {
		this.ca = Math.min(this.ca + dt, 1);

		for (Boss boss : Boss.all) {
			if (boss.talked && !boss.getState().equals("unactive") && !healthbars.containsKey(boss.getClass())) {
				Healthbar healthbar = new Healthbar();
				healthbar.boss = boss;
				healthbar.targetValue = healthbars.size() * 19 + 16;

				healthbars.put(boss.getClass(), healthbar);
			}
		}

		Healthbar[] bars = healthbars.values().toArray(new Healthbar[] {});

		for (int i = bars.length - 1; i >= 0; i--) {
			bars[i].update(dt);

			if (bars[i].done) {
				Log.info(bars[i].boss.getHp() + " dead");
				healthbars.remove(bars[i].boss.getClass());

				int j = 0;

				for (final Healthbar bar : healthbars.values()) {
					bar.targetValue = j * 19 + 16;
					bar.tweened = true;

					Tween.to(new Tween.Task(Display.GAME_HEIGHT - bar.targetValue, 0.5f) {
						@Override
						public float getValue() {
							return bar.y;
						}

						@Override
						public void setValue(float value) {
							bar.y = value;
						}
					});

					j++;
				}
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

		for (Healthbar healthbar : healthbars.values()) {
			healthbar.render();
		}

		if (Dungeon.game.getState() instanceof InGameState) {
			if (Player.instance != null && Player.instance.isDead()) {
				Graphics.print("Game over!", Graphics.medium, 128);
				Graphics.print("Press space to restart", Graphics.medium, (float) (108 + Math.sin(Dungeon.time * 3) * 4));

				if (Input.instance.wasPressed("action")) {
					Dungeon.newGame();
				}
			}
		}
	}

	public void renderCursor() {
		if (Input.instance.activeController != null) {
			return;
		}
		
		float s = (float) (1.2f + Math.cos(Dungeon.time / 1.5f) / 5f) * this.scale;

		//float dx = Math.abs(Input.instance.target.x - Input.instance.mouse.x);
		//float dy = Math.abs(Input.instance.target.y - Input.instance.mouse.y);

		float a = Dungeon.time * 60;

		float sx = 1; //MathUtils.clamp(1f, 2f, dx / 30);
		float sy = 1; //MathUtils.clamp(1f, 2f, dy / 30);

		Graphics.batch.setColor(1, 1, 1, this.ca);
		Graphics.render(this.cursor, Input.instance.uiMouse.x, Input.instance.uiMouse.y, a, 8, 8, false, false, sx * s, sy * s);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	private float ca;

	public void renderUi() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		renderCursor();
	}
}