package org.rexcellentgames.burningknight.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.game.state.MainMenuState;
import org.rexcellentgames.burningknight.game.state.State;
import org.rexcellentgames.burningknight.ui.UiButton;
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

		if (!dead) {
			for (Boss boss : Boss.all) {
				if (boss.talked && !boss.getState().equals("unactive") && !healthbars.containsKey(boss.getClass())) {
					Healthbar healthbar = new Healthbar();
					healthbar.boss = boss;
					healthbar.targetValue = healthbars.size() * 19 + 16;

					healthbars.put(boss.getClass(), healthbar);
				}
			}
		}

		Healthbar[] bars = healthbars.values().toArray(new Healthbar[] {});

		for (int i = bars.length - 1; i >= 0; i--) {
			bars[i].update(dt);

			if (bars[i].done || dead) {
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

	private float al;
	private float val;
	private float size;
	private String kills;
	private float killX = -128;
	private float mainY = -128;
	private String time;
	private float timeW;

	public void onDeath() {
		kills = GameSave.killCount + " kills";

		time = String.format("%02d", (int) Math.floor(GameSave.time / 3600)) + ":" +
			String.format("%02d", (int) Math.floor(GameSave.time / 60)) + ":" +
			String.format("%02d", (int) Math.floor(GameSave.time % 60)) + ":" +
			String.format("%02d", (int) Math.floor(GameSave.time % 1 * 100));

		Graphics.layout.setText(Graphics.small, time);
		this.timeW = Graphics.layout.width;

		Tween.to(new Tween.Task(1, 0.3f) {
			@Override
			public float getValue() {
				return Dungeon.grayscale;
			}

			@Override
			public void setValue(float value) {
				Dungeon.grayscale = value;
			}
		}).delay(0.15f);

		val = 1;

		Tween.to(new Tween.Task(1, 0.05f) {
			@Override
			public float getValue() {
				return al;
			}

			@Override
			public void setValue(float value) {
				al = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.4f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return mainY;
					}

					@Override
					public void setValue(float value) {
						mainY = value;
					}
				});

				Tween.to(new Tween.Task(52, 0.2f) {
					@Override
					public float getValue() {
						return size;
					}

					@Override
					public void setValue(float value) {
						size = value;
					}

					@Override
					public void onEnd() {
						Tween.to(new Tween.Task(0, 0.4f, Tween.Type.BACK_OUT) {
							@Override
							public float getValue() {
								return killX;
							}

							@Override
							public void setValue(float value) {
								killX = value;
							}
						});
					}
				});

				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return al;
					}

					@Override
					public void setValue(float value) {
						al = value;
					}
				});
			}
		});

		UiButton button = (UiButton) Dungeon.ui.add(new UiButton("Restart", Display.GAME_WIDTH / 2 - 256, 107) {
			@Override
			public void onClick() {
				super.onClick();

				rst();
				Dungeon.newGame();
				Camera.shake(3);
			}
		});
		Dungeon.ui.select(button);

		UiButton finalButton = button;
		Tween.to(new Tween.Task(Display.GAME_WIDTH / 2, 0.5f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return finalButton.x;
			}

			@Override
			public void setValue(float value) {
				finalButton.x = value;
			}
		}).delay(0.3f);

		button = (UiButton) Dungeon.ui.add(new UiButton("Menu", Display.GAME_WIDTH / 2 + 256, 83) {
			@Override
			public void onClick() {
				super.onClick();

				rst();
				State.transition(() -> Dungeon.game.setState(new MainMenuState()));
				Camera.shake(3);
			}
		});

		UiButton finalButton1 = button;
		Tween.to(new Tween.Task(Display.GAME_WIDTH / 2, 0.5f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return finalButton1.x;
			}

			@Override
			public void setValue(float value) {
				finalButton1.x = value;
			}
		}).delay(0.3f);
	}

	private void rst() {
		mainY = -128;
		killX = -128;
		size = 0;
	}

	public void render() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		for (Healthbar healthbar : healthbars.values()) {
			healthbar.render();
		}

		if (Dungeon.game.getState() instanceof InGameState) {
			if (this.al > 0) {
				Graphics.startAlphaShape();
				Graphics.shape.setColor(this.val, this.val, this.val, this.al);
				Graphics.shape.rect(0, 0, Display.GAME_WIDTH, Display.GAME_HEIGHT);
				Graphics.endAlphaShape();
			}

			float y = Display.GAME_HEIGHT - 52 - 32;

			if (this.size > 0) {
				Graphics.startShape();
				Graphics.shape.setColor(0, 0, 0, 1);
				Graphics.shape.rect(0, 0, Display.GAME_WIDTH, size);
				Graphics.shape.rect(0, Display.GAME_HEIGHT - size, Display.GAME_WIDTH, size);
				Graphics.endShape();

				if (this.killX != -128) {
					float yy = y - 32;

					Graphics.small.draw(Graphics.batch, this.kills, this.killX + 32, yy);
					Graphics.small.draw(Graphics.batch, this.time, Display.GAME_WIDTH - 32 - this.killX - this.timeW, yy);
				}
			}

			if (this.mainY != -128) {
				Graphics.print("You did not kill the Burning Knight", Graphics.medium, y - 16 + this.mainY);
			}
		}
	}

	public boolean dead;

	public void renderCursor() {
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
}