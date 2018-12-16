package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class MainMenuState extends State {
	public static MainMenuState instance;
	private static TextureRegion logo = new TextureRegion(new Texture(Gdx.files.internal("artwork_logo (sticker).png")));

	private ArrayList<UiButton> buttons = new ArrayList<>();
	private float logoX = 0;
	private float logoY = 0;
	public static float cameraX = Display.UI_WIDTH_MAX / 2;
	public static float cameraY = Display.UI_HEIGHT_MAX / 2;
	public static final float MOVE_T = 0.2f;

	public static UiEntity first;

	public static boolean skip;
	private float pressA;
	private float t;

	public MainMenuState() {
		skip = false;
	}

	public MainMenuState(boolean skip) {
		MainMenuState.skip = skip;
	}

	public static Music voidMusic = Audio.getMusic("Void");

	public void startVoid() {
		voidMusic.setLooping(true);
		voidMusic.setVolume(0);
		voidMusic.play();

		Tween.to(new Tween.Task(Settings.music, 1f) {
			@Override
			public float getValue() {
				return voidMusic.getVolume();
			}

			@Override
			public void setValue(float value) {
				voidMusic.setVolume(value);
			}
		});
	}

	public void endVoid() {
		Tween.to(new Tween.Task(0, 0.2f) {
			@Override
			public float getValue() {
				return voidMusic.getVolume();
			}

			@Override
			public void setValue(float value) {
				voidMusic.setVolume(value);
			}
		});
	}

	@Override
	public void destroy() {
		super.destroy();
		endVoid();
		pauseMenuUi.destroy();
	}

	@Override
	public void init() {
		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.dark = 0;

		Audio.stop();
		Dungeon.setBackground(new Color(0, 0, 0, 1));

		Tween.to(new Tween.Task(1, 0.2f) {
			@Override
			public float getValue() {
				return Dungeon.dark;
			}

			@Override
			public void setValue(float value) {
				Dungeon.dark = value;
			}
		});

		Dungeon.setBackground2(Color.valueOf("#000000"));

		cameraX = Display.UI_WIDTH_MAX / 2;
		cameraY = Display.UI_HEIGHT_MAX / 2;

		startVoid();

		Dungeon.buildDiscordBadge();

		int y = Display.UI_HEIGHT / 2 - 24;

		logoX = 0f;

		instance = this;
		Dungeon.area.add(Camera.instance);
		Camera.target = null;

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("play", -128, (int) (y + 24)) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = Player.Type.values()[GlobalSave.getInt("last_class")];
				GameSave.Info info = GameSave.peek(SaveManager.slot);

				for (final UiButton button : buttons) {
					Tween.to(new Tween.Task(-Display.UI_WIDTH_MAX, skip ? 0.001f : 0.4f, Tween.Type.BACK_OUT) {
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

				Tween.to(new Tween.Task(0, 0.1f) {
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
						Dungeon.loadType = Entrance.LoadType.LOADING;
						Dungeon.goToLevel((info.free ? -2 : info.depth));
					}
				});
			}
		}.setSparks(true)));

		first = buttons.get(0);
		Dungeon.ui.select(first);

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("settings", -128, (int) (y)) {
			@Override
			public void onClick() {
				super.onClick();
				addSettings();

				Tween.to(new Tween.Task(Display.UI_WIDTH, 0.15f, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return settingsX;
					}

					@Override
					public void setValue(float value) {
						settingsX = value;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}.setSparks(true)));

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("exit", -128, (int) (y - 24)) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				transition(new Runnable() {
					@Override
					public void run() {
						Gdx.app.exit();
					}
				});
			}
		}));

		pauseMenuUi = new Area(true);
		this.pauseMenuUi.show();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (logoY == 0) {
			if (this.t >= 3f) {
				this.pressA = Math.min(1, pressA + dt);
			}
		} else {
			pressA = Math.max(0, pressA - dt * 4);
		}

		pauseMenuUi.update(dt);
	}

	@Override
	public void render() {
		super.render();

		if (logoY == 0 && (Input.instance.wasPressed("start"))) {
			Tween.to(new Tween.Task(48, 0.3f) {
				@Override
				public float getValue() {
					return size;
				}

				@Override
				public void setValue(float value) {
					size = value;
				}
			});

			Dungeon.flash(Color.WHITE, 0.05f);
			Audio.stop();
			Audio.highPriority("Menu");
			Audio.current.setLooping(true);

			Tween.to(new Tween.Task(256, 0.7f, Tween.Type.QUAD_IN) {
				@Override
				public float getValue() {
					return 0;
				}

				@Override
				public void setValue(float value) {
					logoY = value;
				}
			});

			for (final UiButton button : buttons) {
				Tween.to(new Tween.Task(Display.UI_WIDTH_MAX / 2, skip ? 0.001f : 0.4f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return button.x;
					}

					@Override
					public void setValue(float value) {
						button.x = value;
					}
				}).delay(0.4f);
			}
		}
	}

	private float size;

	@Override
	public void renderUi() {
		renderPortal();
		super.render();

		Camera.ui.position.set(cameraX, cameraY, 0);
		Camera.ui.update();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (logoY < 256f) {
			float scale = 1f;
			Graphics.batch.setColor(1, 1, 1, 1);
			Graphics.render(logo, Display.UI_WIDTH_MAX / 2 + logoX, (float) (Display.UI_HEIGHT / 2 + Math.cos(Dungeon.time * 3f) * 2.5f) + logoY, 0, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, scale, scale);
		}

		Camera.ui.position.set(Display.UI_WIDTH / 2, Display.UI_HEIGHT / 2, 0);
		Camera.ui.update();

		Camera.ui.translate(settingsX, 0);
		Camera.ui.update();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Dungeon.ui.render();
		pauseMenuUi.render();

		Camera.ui.translate(-settingsX, 0);
		Camera.ui.update();

		Graphics.startShape();
		Graphics.shape.setColor(0, 0, 0, 1);
		Graphics.shape.rect(0, 0, Display.UI_WIDTH, size);
		Graphics.shape.rect(0, Display.UI_HEIGHT - size, Display.UI_WIDTH, size);
		Graphics.endShape();

		Graphics.small.setColor(1, 1, 1, this.pressA);
		Graphics.printCenter("Press space to start", Graphics.small, 0, (float) (20));
		Graphics.small.setColor(1, 1, 1, 1);

		Camera.ui.position.set(cameraX, cameraY, 0);
		Camera.ui.update();

		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Ui.ui.renderCursor();
	}
}