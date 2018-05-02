package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;

public class SettingsState extends State {
	public SettingsState() {

	}

	private boolean fromGame;

	public SettingsState(boolean fromGame) {
		this.fromGame = fromGame;
	}

	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("menu_label (graphics)", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				super.onClick();

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.game.setState(new GraphicsSettingsState());
						Camera.instance.shake(3);
					}
				});
			}
		}.setSparks(true));

		Dungeon.area.add(new UiButton("menu_label (audio)", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.game.setState(new AudioSettingsState());
						Camera.instance.shake(3);
					}
				});

			}
		}.setSparks(true));

		Dungeon.area.add(new UiButton("menu_label (back)", Display.GAME_WIDTH / 2, 128 - 24 * 2) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");

				transition(new Runnable() {
					@Override
					public void run() {
						if (fromGame) {
							Dungeon.goToLevel(-1);
						} else {
							Dungeon.game.setState(new MainMenuState());
						}

						Camera.instance.shake(3);
					}
				});
			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}