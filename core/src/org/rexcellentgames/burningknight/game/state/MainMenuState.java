package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class MainMenuState extends State {
	public static MainMenuState instance;
	private static TextureRegion logo = Graphics.getTexture("artwork_logo (sticker)");
	private ArrayList<UiButton> buttons = new ArrayList<>();
	private float logoX = 0;
	private float logoY = Display.GAME_HEIGHT;
	private float versionX;
	private float versionY = -32;
	public static float cameraX = Display.GAME_WIDTH / 2;
	public static float cameraY = Display.GAME_HEIGHT / 2;
	public static float MOVE_T = 0.2f;

	@Override
	public void init() {
		Dungeon.buildDiscordBadge();

		instance = this;
		Dungeon.area.add(Camera.instance);
		Camera.target = null;

		buttons.add((UiButton) Dungeon.area.add(new UiButton("play", -128, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();
				SlotSelectState.add();

				Tween.to(new Tween.Task(-Display.GAME_HEIGHT / 2, MainMenuState.MOVE_T) {
					@Override
					public float getValue() {
						return cameraY;
					}

					@Override
					public void setValue(float value) {
						cameraY = value;
					}
				});
			}
		}.setSparks(true)));

		buttons.add((UiButton) Dungeon.area.add(new UiButton("settings", Display.GAME_WIDTH + 128, (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				super.onClick();
				SettingsState.add();

				Tween.to(new Tween.Task(Display.GAME_WIDTH * 1.5f, MainMenuState.MOVE_T) {
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

		buttons.add((UiButton) Dungeon.area.add(new UiButton("exit", -128, (int) (128 - 24 * 3.5f)) {
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

		Tween.to(new Tween.Task(0, 1f, Tween.Type.BACK_OUT) {
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

		Tween.to(new Tween.Task(0, MainMenuState.MOVE_T) {
			@Override
			public float getValue() {
				return versionY;
			}

			@Override
			public void setValue(float value) {
				versionY = value;
			}
		}).delay(0.7f);
	}

	@Override
	public void render() {
		super.render();

		Camera.ui.position.set(cameraX, cameraY, 0);
		Camera.ui.update();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		float sx = (float) (0.8f + Math.sin(Dungeon.time / 1.5f) / 40);
		float sy = (float) (0.8f + Math.cos(Dungeon.time) / 40);
		float a = (float) (Math.cos(Dungeon.time * 0.7f) * 3f);

		Graphics.render(logo, Display.GAME_WIDTH / 2 + logoX, 180 + logoY, a, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, sx, sy);
		Graphics.print(Version.string, Graphics.small, 2 + versionX, versionY + 2);

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Ui.ui.renderCursor();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
	}
}