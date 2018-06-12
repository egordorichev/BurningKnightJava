package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class SettingsState extends State {
	public static boolean fromGame;
	private ArrayList<UiButton> buttons = new ArrayList<>();
	private boolean twe;

	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		buttons.add((UiButton) Dungeon.area.add(new UiButton("graphics", 400, 128 + 24) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.game.setState(new GraphicsSettingsState());
				});
			}
		}.setSparks(true)));

		buttons.add((UiButton) Dungeon.area.add(new UiButton("audio", 400, 128) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.game.setState(new AudioSettingsState());
				});

			}
		}.setSparks(true)));

		buttons.add((UiButton) Dungeon.area.add(new UiButton("input", 400, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.game.setState(new InputSettingsState());
				});

			}
		}.setSparks(true)));

		buttons.add((UiButton) Dungeon.area.add(new UiButton("back", 400, (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");

				for (UiButton button : buttons) {
					Tween.to(new Tween.Task(400, 0.3f) {
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
							if (!twe) {
								twe = true;

								MainMenuState.fromRight = true;
								Dungeon.game.setState(new MainMenuState());
							}
						}
					});
				}
			}
		}));

		for (UiButton button : buttons) {
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
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}