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
	public static MainMenuState instance;
	private static TextureRegion logo = Graphics.getTexture("artwork_logo (sticker)");
	private float logoY = Display.GAME_HEIGHT;
	private ArrayList<UiButton> buttons = new ArrayList<>();
	private float versionY = -32;
	private float logoX = 0;
	private float versionX;

	@Override
	public void init() {
		instance = this;
		Dungeon.area.add(Camera.instance);

		buttons.add((UiButton) Dungeon.area.add(new UiButton("play", -128, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();

				if (!SlotSelectState.added) {
					SlotSelectState.add();
				}

				Tween.to(new Tween.Task(-Display.GAME_HEIGHT / 2, 0.4f) {
					@Override
					public float getValue() {
						return Camera.game.position.y;
					}

					@Override
					public void setValue(float value) {
						Camera.game.position.y = value;
					}
				});
			}
		}.setSparks(true)));

		buttons.add((UiButton) Dungeon.area.add(new UiButton("settings", Display.GAME_WIDTH + 128, (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				super.onClick();

				SettingsState.fromGame = false;
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

		Tween.to(new Tween.Task(0, 0.4f) {
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

		float sx = (float) (0.8f + Math.sin(Dungeon.time / 1.5f) / 40);
		float sy = (float) (0.8f + Math.cos(Dungeon.time) / 40);
		float a = (float) (Math.cos(Dungeon.time * 0.7f) * 3f);

		Graphics.render(logo, Display.GAME_WIDTH / 2 + logoX, 180 + logoY, a, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, sx, sy);

		Graphics.print(Version.string, Graphics.small, 2 + versionX, versionY + 2);
		Ui.ui.renderCursor();
	}
}