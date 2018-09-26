package org.rexcellentgames.burningknight.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.game.state.MainMenuState;
import org.rexcellentgames.burningknight.game.state.State;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.HashMap;

public class Ui {
	public static Ui ui;
	private float scale = 1f;
	public HashMap<Class<? extends Boss>, Healthbar> healthbars = new HashMap<>();

	private TextureRegion cursor;
	private TextureRegion upgrade;

	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui-cursor");
		upgrade = Graphics.getTexture("ui-upgrade_cursor");
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
				healthbars.remove(bars[i].boss.getClass());

				int j = 0;

				for (final Healthbar bar : healthbars.values()) {
					bar.targetValue = j * 19 + 16;
					bar.tweened = true;

					Tween.to(new Tween.Task(Display.UI_HEIGHT - bar.targetValue, 0.5f) {
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

		if (Input.instance.wasPressed("use") || Input.instance.wasPressed("second_use")) {
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
	private String depth;

	private static String killsLocale = Locale.get("kills");

	public void onDeath() {
		Tween.to(new Tween.Task(0, 1f) {
			@Override
			public void onEnd() {
				depth = Dungeon.level.formatDepth();
				kills = GameSave.killCount + " " + killsLocale;

				time = String.format("%02d", (int) Math.floor(GameSave.time / 3600)) + ":" +
					String.format("%02d", (int) Math.floor(GameSave.time / 60)) + ":" +
					String.format("%02d", (int) Math.floor(GameSave.time % 60)) + ":" +
					String.format("%02d", (int) Math.floor(GameSave.time % 1 * 100));

				Graphics.layout.setText(Graphics.small, time);
				timeW = Graphics.layout.width;

				Tween.to(new Tween.Task(1f, 0.3f) {
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

				Audio.play("Nostalgia");
				Audio.reset();

				UiButton button = (UiButton) Dungeon.ui.add(new UiButton("restart", Display.UI_WIDTH / 2 + 256, 107 + 24) {
					@Override
					public void onClick() {
						super.onClick();

						rst();
						Dungeon.newGame(true, 1);
						Camera.shake(3);
					}
				});

				final UiButton finalButton3 = button;
				Tween.to(new Tween.Task(Display.UI_WIDTH / 2, 0.5f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return finalButton3.x;
					}

					@Override
					public void setValue(float value) {
						finalButton3.x = value;
					}
				}).delay(0.3f);

				Dungeon.ui.select(button);

				button = (UiButton) Dungeon.ui.add(new UiButton("back_to_castle", Display.UI_WIDTH / 2 - 256, 107) {
					@Override
					public void onClick() {
						super.onClick();

						rst();
						Dungeon.newGame(true, -1);
						Camera.shake(3);
					}
				});

				Dungeon.ui.select(button);

				UiButton finalButton = button;
				Tween.to(new Tween.Task(Display.UI_WIDTH / 2, 0.5f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return finalButton.x;
					}

					@Override
					public void setValue(float value) {
						finalButton.x = value;
					}
				}).delay(0.3f);

				button = (UiButton) Dungeon.ui.add(new UiButton("menu", Display.UI_WIDTH / 2 + 256, 83) {
					@Override
					public void onClick() {
						super.onClick();

						rst();
						State.transition(() -> Dungeon.game.setState(new MainMenuState()));
						Camera.shake(3);
					}
				});

				UiButton finalButton1 = button;
				Tween.to(new Tween.Task(Display.UI_WIDTH / 2, 0.5f, Tween.Type.BACK_OUT) {
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
		});
	}

	private void rst() {
		mainY = -128;
		killX = -128;

		Tween.to(new Tween.Task(0, 0.3f) {
			@Override
			public float getValue() {
				return Dungeon.grayscale;
			}

			@Override
			public void setValue(float value) {
				Dungeon.grayscale = value;
			}
		});

		Tween.to(new Tween.Task(0, 0.3f) {
			@Override
			public float getValue() {
				return size;
			}

			@Override
			public void setValue(float value) {
				size = value;
			}
		});
	}

	private static TextureRegion coin;
	public static float y;

	public void render() {
		if (coin == null) {
			coin = Graphics.getTexture("ui-coin");
		}

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		for (Healthbar healthbar : healthbars.values()) {
			healthbar.render();
		}

		if (Dungeon.game.getState() instanceof InGameState) {
			if (Dungeon.depth <= -1 || y > 0) {
				Graphics.render(coin, 2, Display.UI_HEIGHT - (Dungeon.depth <= -1 ? 20 : y) + 2);
				Graphics.print(GlobalSave.getInt("num_coins") + "",
					Graphics.medium, 20,
					Display.UI_HEIGHT - (Dungeon.depth <= -1 ? 20 : y));
			}

			if (this.al > 0.05f) {
				Graphics.startAlphaShape();
				Graphics.shape.setColor(this.val, this.val, this.val, this.al);
				Graphics.shape.rect(0, 0, Display.UI_WIDTH, Display.UI_HEIGHT);
				Graphics.endAlphaShape();
			}

			float y = Display.UI_HEIGHT - 52 - 32;

			if (this.size > 0) {
				Graphics.startShape();
				Graphics.shape.setColor(0, 0, 0, 1);
				Graphics.shape.rect(0, 0, Display.UI_WIDTH, size);
				Graphics.shape.rect(0, Display.UI_HEIGHT - size, Display.UI_WIDTH, size);
				Graphics.endShape();

				if (this.killX != -128) {
					float yy = y - 32;

					Graphics.small.draw(Graphics.batch, this.depth, this.killX + 32, yy - 16);
					Graphics.small.draw(Graphics.batch, this.kills, this.killX + 32, yy);
					Graphics.small.draw(Graphics.batch, this.time, Display.UI_WIDTH - 32 - this.killX - this.timeW, yy);
				}
			}

			if (this.mainY != -128) {
				Graphics.print(GameSave.defeatedBK ? kill2Locale : killLocale, Graphics.medium, y - 16 + this.mainY);
			}
		}
	}

	private static String killLocale = Locale.get("didnt_kill_bk");
	private static String kill2Locale = Locale.get("killed_yet_dead");
	private static String hintStr = Locale.get("upgrade_hint");

	public boolean dead;
	public static boolean move;
	public static boolean upgradeMouse;
	public static boolean drawHint = true;
	private static float alf;

	public void renderCursor() {
		if (upgradeMouse) {
			// (move ? Math.cos(Dungeon.time * 6) * 1.5f : 0))

			Graphics.batch.setColor(1, 1, 1, this.ca);
			Graphics.render(this.upgrade, Input.instance.uiMouse.x,
				Input.instance.uiMouse.y, 0, this.upgrade.getRegionWidth() / 2, this.upgrade.getRegionHeight(), false, false);
			Graphics.batch.setColor(1, 1, 1, 1);

			alf += ((move ? 1 : 0) - alf) * Gdx.graphics.getDeltaTime() * 8;

			if (alf > 0.05f && drawHint) {
				Graphics.medium.setColor(0.3f, 1f, 0.3f, alf);
				Graphics.print(hintStr, Graphics.medium, Input.instance.uiMouse.x + 8, Input.instance.uiMouse.y - 20);
				Graphics.medium.setColor(1, 1, 1, 1);
			}
		} else {
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);

			float s = (float) (1.2f + Math.cos(Dungeon.time / 1.5f) / 5f) * this.scale;
			float a = Dungeon.time * 60;

			Graphics.batch.setColor(1, 1, 1, this.ca);
			Graphics.render(this.cursor, Input.instance.uiMouse.x,
				Input.instance.uiMouse.y, a, 8, 8, false, false, s, s);
			Graphics.batch.setColor(1, 1, 1, 1);
		}
	}

	private float ca;
}