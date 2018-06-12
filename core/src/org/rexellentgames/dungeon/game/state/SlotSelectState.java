package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class SlotSelectState extends State {
	private ArrayList<UiButton> buttons = new ArrayList<>();
	private float tty = -300;
	private boolean twe;

	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		buttons.add((UiButton) Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 2.5f) - 300) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");

				Tween.to(new Tween.Task(-300, 0.2f) {
					@Override
					public float getValue() {
						return tty;
					}

					@Override
					public void setValue(float value) {
						tty = value;
					}
				});

				for (UiButton button : buttons) {
					Tween.to(new Tween.Task(button.y - 300, 0.1f) {
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
							if (!twe) {
								twe = true;

								MainMenuState.fromBottom = true;
								Dungeon.game.setState(new MainMenuState());
							}
						}
					});
				}
			}
		}));

		for (UiButton button : buttons) {
			Tween.to(new Tween.Task(button.y + 300, 0.2f) {
				@Override
				public float getValue() {
					return button.y;
				}

				@Override
				public void setValue(float value) {
					button.y = value;
				}
			});
		}

		Tween.to(new Tween.Task(0, 0.2f) {
			@Override
			public float getValue() {
				return tty;
			}

			@Override
			public void setValue(float value) {
				tty = value;
			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Graphics.print("Select slot", Graphics.medium, tty + Display.GAME_HEIGHT - 64);
		Ui.ui.renderCursor();
	}
}