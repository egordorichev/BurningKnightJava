package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Version;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class MainMenuState extends State {
	private static TextureRegion logo = Graphics.getTexture("artwork_logo (sticker)");
	public static MainMenuState instance;
	private float ly = Display.GAME_HEIGHT;
	private ArrayList<UiButton> buttons = new ArrayList<>();
	private float vy = -32;
	private float vx = 0;
	public static boolean fromBottom;
	public static boolean fromRight;

	@Override
	public void init() {
		instance = this;

		Dungeon.area.add(Camera.instance);

		buttons.add((UiButton) Dungeon.area.add(new UiButton("play", -128, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();

				Tween.to(new Tween.Task(-100, 0.1f) {
					@Override
					public float getValue() {
						return vvx;
					}

					@Override
					public void setValue(float value) {
						vvx = value;
					}
				});

				Tween.to(new Tween.Task(250, 0.3f) {
					@Override
					public float getValue() {
						return ly;
					}

					@Override
					public void setValue(float value) {
						ly = value;
					}
				});

				for (UiButton button : buttons) {
					Tween.to(new Tween.Task(300, 0.3f) {
						@Override
						public float getValue() {
							return button.y;
						}

						@Override
						public void setValue(float value) {
							button.y = value;
						}

						@Override
						public void onEnd() {
							Dungeon.game.setState(new SlotSelectState());
						}
					});
				}
			}
		}.setSparks(true)));

		buttons.add((UiButton) Dungeon.area.add(new UiButton("settings", Display.GAME_WIDTH + 128, (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				super.onClick();

				SettingsState.fromGame = false;

				Tween.to(new Tween.Task(-100, 0.1f) {
					@Override
					public float getValue() {
						return vvx;
					}

					@Override
					public void setValue(float value) {
						vvx = value;
					}
				});

				for (UiButton button : buttons) {
					Tween.to(new Tween.Task(-300, 0.3f) {
						@Override
						public float getValue() {
							return button.x;
						}

						@Override
						public void setValue(float value) {
							button.x = value;
						}

						@Override
						public void onEnd() {
							Dungeon.game.setState(new SettingsState());
						}
					});
				}

				Tween.to(new Tween.Task(-300, 0.3f) {
					@Override
					public float getValue() {
						return vx;
					}

					@Override
					public void setValue(float value) {
						vx = value;
					}
				});
			}
		}));

		buttons.add((UiButton) Dungeon.area.add(new UiButton("exit", -128, (int) (128 - 24 * 3.5f)) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");

				transition(() -> {
					Gdx.app.exit();
				});
			}
		}));

		if (fromBottom) {
			fromBottom = false;

			for (UiButton button : buttons) {
				button.x = Display.GAME_WIDTH / 2;

				int y = (int) button.y;
				button.y = 300;

				Tween.to(new Tween.Task(y, 0.1f) {
					@Override
					public float getValue() {
						return button.y;
					}

					@Override
					public void setValue(float value) {
						button.y = value;
					}
				});

				ly = 200;

				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return ly;
					}

					@Override
					public void setValue(float value) {
						ly = value;
					}
				});
			}
		} else if (fromRight) {
			fromRight = false;

			for (UiButton button : buttons) {
				button.x = Display.GAME_WIDTH / 2 - 300;

				Tween.to(new Tween.Task(Display.GAME_WIDTH / 2, 0.1f) {
					@Override
					public float getValue() {
						return button.x;
					}

					@Override
					public void setValue(float value) {
						button.x = value;
					}
				});
			}

			vx = Display.GAME_WIDTH / 2 - 300;
			ly = 0;

			Tween.to(new Tween.Task(0, 0.1f) {
				@Override
				public float getValue() {
					return vx;
				}

				@Override
				public void setValue(float value) {
					vx = value;
				}
			});
		} else {
			Tween.to(new Tween.Task(0, 1f, Tween.Type.BACK_OUT) {
				@Override
				public float getValue() {
					return ly;
				}

				@Override
				public void setValue(float value) {
					ly = value;
				}

				@Override
				public void onEnd() {
					super.onEnd();

					for (UiButton button : buttons) {
						Tween.to(new Tween.Task(Display.GAME_WIDTH / 2, 0.6f, Tween.Type.BACK_OUT) {
							@Override
							public float getValue() {
								return button.x;
							}

							@Override
							public void setValue(float value) {
								button.x = value;
							}
						});
					}
				}
			});
		}

		Tween.to(new Tween.Task(0, 0.4f) {
			@Override
			public float getValue() {
				return vy;
			}

			@Override
			public void setValue(float value) {
				vy = value;
			}
		}).delay(0.7f);
	}

	@Override
	public void render() {
		super.render();

		float sx = (float) (0.8f + Math.sin(Dungeon.time / 1.5f) / 40);
		float sy = (float) (0.8f + Math.cos(Dungeon.time) / 40);
		float a = (float) (Math.cos(Dungeon.time * 0.7f) * 3f);

		Graphics.render(logo, Display.GAME_WIDTH / 2 + vx, 180 + ly, a, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, sx, sy);

		Graphics.print(Version.string, Graphics.small, 2 + vvx, vy + 2);
		Ui.ui.renderCursor();
	}

	private float vvx;
}