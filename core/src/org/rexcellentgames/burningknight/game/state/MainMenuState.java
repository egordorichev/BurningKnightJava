package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Noise;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.ColorUtils;
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

		int y = Display.UI_HEIGHT / 2 - 24;

		float v = 0;
		logoX = v;

		instance = this;
		Dungeon.area.add(Camera.instance);
		Camera.target = null;

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("play", -128, (int) (y + 24)) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = Player.Type.values()[GlobalSave.getInt("last_class")];
				GameSave.Info info = GameSave.peek(SaveManager.slot);

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.loadType = Entrance.LoadType.LOADING;
						Dungeon.goToLevel(GlobalSave.isTrue("finished_tutorial") ? (info.free ? -2 : info.depth) : -3);
					}
				});
			}
		}.setSparks(true)));

		first = buttons.get(0);
		Dungeon.ui.select(first);

		buttons.add((UiButton) Dungeon.ui.add(new UiButton("settings", (int) (Display.UI_WIDTH_MAX + 128 + v), (int) (y)) {
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
	}

	@Override
	public void render() {
		super.render();

		Graphics.startAlphaShape();
		Graphics.shape.setProjectionMatrix(Camera.nil.combined);
		Color cl = ColorUtils.HSV_to_RGB(Dungeon.time * 20 % 360, 360, 360);
		Dungeon.setBackground2(new Color(cl.r * 0.4f, cl.g * 0.4f, cl.b * 0.4f, 1f));

		for (int i = 0; i < 65; i++) {
			float s = i * 0.015f;
			float mx = (Noise.instance.noise(Dungeon.time * 0.25f + s) * 128f);
			float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + s) * 128f);
			float v = ((float) i) / 80f + 0.3f;

			Color color = ColorUtils.HSV_to_RGB((Dungeon.time * 20 - i * 1.4f) % 360, 360, 360);
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.5f);

			float a = (float) (Math.PI * i * 0.2f) + Dungeon.time * 2f;
			float w = i * 2 + 64;
			float d = i * 4f;
			float x = (float) (Math.cos(a) * d) + Display.GAME_WIDTH / 2 + mx * (((float) 56-i) / 56);
			float y = (float) (Math.sin(a) * d) + Display.GAME_HEIGHT / 2 + my * (((float) 56-i) / 56);

			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 1f, 1f, (float) Math.toDegrees(a + 0.1f));
			Graphics.shape.setColor(v * color.r, v * color.g, v * color.b, 0.9f);
			Graphics.shape.rect(x - w / 2, y - w / 2, w / 2, w / 2, w, w, 0.9f, 0.9f, (float) Math.toDegrees(a + 0.1f));
		}

		float i = 32;
		float mx = (Noise.instance.noise(Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);
		float my = (Noise.instance.noise( 3 + Dungeon.time * 0.25f + i * 0.015f + 0.1f) * 128f) * (((float) 56-i) / 56);

		Graphics.endAlphaShape();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Graphics.render(InventoryState.player, Display.GAME_WIDTH / 2 + mx, Display.GAME_HEIGHT / 2 + my, Dungeon.time * 650, 8, 8,false, false);

		if (false && logoY == 0 && (Input.instance.wasPressed("use") ||
			Input.instance.wasPressed("X") || Input.instance.wasPressed("Return"))) {

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

	@Override
	public void renderUi() {
		super.render();

		Camera.ui.position.set(cameraX, cameraY, 0);
		Camera.ui.update();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (logoY < 256f) {
			float scale = 1f;
			Graphics.batch.setColor(1, 1, 1, 1);
			Graphics.render(logo, Display.UI_WIDTH_MAX / 2 + logoX, (float) (Display.UI_HEIGHT / 2 + Math.cos(Dungeon.time * 3f) * 2.5f) + logoY, 0, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, scale, scale);
		}

		Dungeon.ui.render();

		float size = 48f;

		Camera.ui.position.set(Display.UI_WIDTH / 2, Display.UI_HEIGHT / 2, 0);
		Camera.ui.update();

		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		Graphics.startShape();
		Graphics.shape.setColor(0, 0, 0, 1);
		Graphics.shape.rect(0, 0, Display.UI_WIDTH, size);
		Graphics.shape.rect(0, Display.UI_HEIGHT - size, Display.UI_WIDTH, size);
		Graphics.endShape();

		Camera.ui.position.set(cameraX, cameraY, 0);
		Camera.ui.update();

		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Ui.ui.renderCursor();
	}
}