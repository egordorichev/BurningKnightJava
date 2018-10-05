package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.fx.BackgroundFx;
import org.rexcellentgames.burningknight.game.fx.PixelFx;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class MainMenuState extends State {
	public static MainMenuState instance;
	private static TextureRegion logo = new TextureRegion(new Texture(Gdx.files.internal("artwork_logo (sticker).png")));
	private ArrayList<UiButton> buttons = new ArrayList<>();
	private float logoX = 0;
	private float logoY = Display.UI_HEIGHT_MAX;
	public static float cameraX = Display.UI_WIDTH_MAX / 2;
	public static float cameraY = Display.UI_HEIGHT_MAX / 2;
	public static final float MOVE_T = 0.2f;

	private float scale = 1f;
	public static UiEntity first;

	public static boolean skip;

	public MainMenuState() {
		skip = false;
	}

	public MainMenuState(boolean skip) {
		MainMenuState.skip = skip;
	}

	@Override
	public void init() {
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

		Dungeon.setBackground2(Color.valueOf("#000000")); // 1a1932

		SettingsState.added = false;
		InputSettingsState.added = false;
		GraphicsSettingsState.added = false;
		AudioSettingsState.added = false;
		KeyConfigState.added = false;

		cameraX = Display.UI_WIDTH_MAX / 2;
		cameraY = Display.UI_HEIGHT_MAX / 2;

		String track = "Burning Knight";
		Audio.play(track);

		Dungeon.buildDiscordBadge();

		int y = -24;

		float v = 0;
		logoX = v;

		instance = this;
		Dungeon.area.add(Camera.instance);
		Camera.target = null;

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("play", -128, 128 - 24 + y) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = Player.Type.values()[GlobalSave.getInt("last_class")];
				GameSave.Info info = GameSave.peek(SaveManager.slot);

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.loadType = Entrance.LoadType.LOADING;
						Dungeon.goToLevel(GlobalSave.isTrue("finished_tutorial") ? (info.free ? -1 : info.depth) : -3);
					}
				});
			}
		}.setSparks(true)));

		first = buttons.get(0);
		Dungeon.ui.select(first);

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("settings", (int) (Display.UI_WIDTH_MAX + 128 + v), (int) (128 - 24 * 2.5f + y)) {
			@Override
			public void onClick() {
				super.onClick();
				SettingsState.add();
				Dungeon.ui.select(SettingsState.first);

				Tween.to(new Tween.Task(Display.UI_WIDTH_MAX * 1.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return cameraX;
					}

					@Override
					public void setValue(float value) {
						cameraX = value;
					}
				});
			}
		}));

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("exit", -128, (int) (128 - 24 * 3.5f + y)) {
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

		Tween.to(new Tween.Task(0, skip ? 0.001f : 0.6f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return logoY;
			}

			@Override
			public void setValue(float value) {
				logoY = value;
			}

			@Override
			public void onEnd() {
				super.onEnd();

				for (final UiButton button : buttons) {
					Tween.to(new Tween.Task(Display.UI_WIDTH_MAX / 2 + v, skip ? 0.001f : 0.4f, Tween.Type.BACK_OUT) {
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

		// Particles

		Camera.game.position.set(Display.UI_WIDTH_MAX / 2, Display.UI_HEIGHT_MAX / 2, 0);
		Camera.game.update();

		for (int i = 0; i < 100; i++) {
			BackgroundFx fx = new BackgroundFx();
			Dungeon.area.add(fx);

			fx.y = Random.newFloat(-32, Display.UI_HEIGHT_MAX + 32);
		}

		spread();
	}

	private void spread() {
		if (!logo.getTexture().getTextureData().isPrepared()) {
			logo.getTexture().getTextureData().prepare();
		}

		if (!InGameState.noise.getTexture().getTextureData().isPrepared()) {
			InGameState.noise.getTexture().getTextureData().prepare();
		}

		Pixmap pixmap = logo.getTexture().getTextureData().consumePixmap();
		Pixmap noise = InGameState.noise.getTexture().getTextureData().consumePixmap();

		int h = logo.getRegionHeight();

		float lx = Display.UI_WIDTH_MAX / 2 - logo.getRegionWidth() / 2;
		float ly = 180 - 3 - h / 2;

		for (int ry = 0; ry < h; ry++) {
			for (int rx = 0; rx < logo.getRegionWidth(); rx++) {
				Color color = new Color(pixmap.getPixel(rx, (h - ry)));

				if (color.r != 0 || color.g != 0 || color.b != 0) {
					PixelFx fx = new PixelFx();


					float a = (float) (new Color(noise.getPixel(rx, ry)).r * Math.PI);
					fx.vel.x = (float) Math.cos(a) * 90f;
					fx.vel.y = (float) Math.sin(a) * 90f;

					fx.x = lx + rx;
					fx.y = ly + ry;

					fx.r = color.r;
					fx.g = color.g;
					fx.b = color.b;

					Dungeon.ui.add(fx);
				}
			}
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.wasPressed("Space")) {
			spread();
		}
	}

	@Override
	public void renderUi() {
		super.render();

		Camera.ui.position.set(cameraX, cameraY, 0);
		Camera.ui.update();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		// Graphics.render(logo, Display.UI_WIDTH_MAX / 2 + logoX, 180 + logoY - 3, 0, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, scale, scale);
		Dungeon.ui.render();

		Ui.ui.renderCursor();
	}
}