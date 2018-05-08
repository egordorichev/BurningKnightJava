package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Part;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.InGameState;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Ui {
	public static Ui ui;

	private TextureRegion cursor;
	private TextureRegion frame = Graphics.getTexture("ui-bkbar-frame");
	private TextureRegion bar = Graphics.getTexture("ui-bkbar-fill");
	private TextureRegion skull = Graphics.getTexture("ui-bkbar-skull");
	private TextureRegion lock = Graphics.getTexture("ui-bkbar-lock");
	private Animation animations = Animation.make("ui-bkbar-flame");
	private float scale = 1f;

	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui (cursor)");
	}

	private float last;

	public void update(float dt) {
		this.last += dt;

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

	public float y = Display.GAME_HEIGHT;
	private boolean tweened = false;
	private float lastV;
	private float lastBV;
	private float max = 1000;
	private float sx = 1;
	private float sy = 1;

	public void render() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (Dungeon.game.getState() instanceof InGameState) {
			if (BurningKnight.instance != null) {
				if (((int) this.lastBV) != BurningKnight.instance.getHp()) {
					Tween.to(new Tween.Task(0.95f, 0.1f) {
						@Override
						public float getValue() {
							return sx;
						}

						@Override
						public void setValue(float value) {
							sx = value;
						}

						@Override
						public void onEnd() {
							Tween.to(new Tween.Task(1f, 0.3f, Tween.Type.BACK_OUT) {
								@Override
								public float getValue() {
									return sx;
								}

								@Override
								public void setValue(float value) {
									sx = value;
								}
							});
						}
					});

					Tween.to(new Tween.Task(1.1f, 0.1f) {
						@Override
						public float getValue() {
							return sy;
						}

						@Override
						public void setValue(float value) {
							sy = value;
						}

						@Override
						public void onEnd() {
							Tween.to(new Tween.Task(1f, 0.3f, Tween.Type.BACK_OUT) {
								@Override
								public float getValue() {
									return sy;
								}

								@Override
								public void setValue(float value) {
									sy = value;
								}
							});
						}
					});
				}

				max = BurningKnight.instance.getHpMax();
				this.lastV += (BurningKnight.instance.getHp() - this.lastV) / 20f;
				this.lastBV += (BurningKnight.instance.getHp() - this.lastBV) / 4f;

				boolean d = BurningKnight.instance.isDead() || BurningKnight.instance.getState().equals("unactive");

				if (d && this.tweened) {
					tweened = false;
					Tween.to(new Tween.Task(Display.GAME_HEIGHT, 0.5f) {
						@Override
						public float getValue() {
							return y;
						}

						@Override
						public void setValue(float value) {
							y = value;
						}
					});
				} else if (!d && !this.tweened) {
					tweened = true;

					Tween.to(new Tween.Task(Display.GAME_HEIGHT - 16, 0.5f, Tween.Type.BACK_OUT) {
						@Override
						public float getValue() {
							return y;
						}

						@Override
						public void setValue(float value) {
							y = value;
						}
					}.delay(0.1f));
				}
			} else if (tweened) {
				tweened = false;
				Tween.to(new Tween.Task(Display.GAME_HEIGHT, 0.3f) {
					@Override
					public float getValue() {
						return y;
					}

					@Override
					public void setValue(float value) {
						y = value;
					}
				});
			}

			if (y != Display.GAME_HEIGHT) {
				TextureRegion r = new TextureRegion(bar);

				Graphics.batch.setColor(0, 0, 0, 1);
				Graphics.render(r, Display.GAME_WIDTH / 2, y + bar.getRegionHeight(), 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);
				Graphics.batch.setColor(0.5f, 0.5f, 0.5f, 1);

				r.setRegionWidth((int) Math.ceil(this.lastV / max * bar.getRegionWidth()));
				Graphics.render(r, Display.GAME_WIDTH / 2, y + bar.getRegionHeight(), 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);

				Graphics.batch.setColor(1, 1, 1, 1);

				float s = this.lastBV / max * bar.getRegionWidth();

				r.setRegionWidth((int) Math.ceil(s));
				Graphics.render(r, Display.GAME_WIDTH / 2, y + bar.getRegionHeight(), 0, bar.getRegionWidth() / 2, bar.getRegionHeight(), false, false, sx, sy);

				/*for (int i = 0; i < 6; i++) {
					Graphics.render(lock, Display.GAME_WIDTH / 2 - bar.getRegionWidth() / 2 + i * lock.getRegionWidth() + lock.getRegionWidth() / 2, y + lock.getRegionHeight() / 2, 0, lock.getRegionWidth() / 2, bar.getRegionHeight() / 2, false, false);
				}*/

				Graphics.render(frame, Display.GAME_WIDTH / 2, y + frame.getRegionHeight() - 5, 0, frame.getRegionWidth() / 2, frame.getRegionHeight(), false, false, sx, sy);

				// todo: scale?
				Graphics.render(skull, Display.GAME_WIDTH / 2 - bar.getRegionWidth() / 2 + s, y + 2, 0, skull.getRegionWidth() - 5, skull.getRegionHeight() / 2, false, false, sx, sy);

				if (this.last > 0.2f) {
					Part part = new Part();
					this.last = 0;
					part.x = Random.newFloat(r.getRegionWidth()) + Display.GAME_WIDTH / 2 - bar.getRegionWidth() / 2;
					part.y = -Random.newFloat(bar.getRegionHeight() * 1.5f) + y + bar.getRegionHeight();
					part.depth = 32;
					part.alwaysRender = true;
					part.alwaysActive = true;
					part.animation = animations.get("idle");
					part.vel = new Point(0, 0.6f);

					Dungeon.ui.add(part);
				}
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

		float dx = Math.abs(Input.instance.target.x - Input.instance.mouse.x);
		float dy = Math.abs(Input.instance.target.y - Input.instance.mouse.y);

		float a = Dungeon.time * 60;

		float sx = MathUtils.clamp(1f, 2f, dx / 30);
		float sy = MathUtils.clamp(1f, 2f, dy / 30);

		Graphics.render(this.cursor, Input.instance.uiMouse.x,
			Input.instance.uiMouse.y, a, 8, 8, false, false, sx * s, sy * s);
	}

	public void renderUi() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
	}
}