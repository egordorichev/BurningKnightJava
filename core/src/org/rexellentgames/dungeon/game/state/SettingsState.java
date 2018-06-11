package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;

public class SettingsState extends State {
	public static boolean fromGame;

	public SettingsState() {

	}

	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("graphics", Display.GAME_WIDTH / 2, 128 + 24) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.game.setState(new GraphicsSettingsState());
					Camera.instance.shake(3);
				});
			}
		}.setSparks(true));

		Dungeon.area.add(new UiButton("audio", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.game.setState(new AudioSettingsState());
					Camera.instance.shake(3);
				});

			}
		}.setSparks(true));

		Dungeon.area.add(new UiButton("input", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.game.setState(new InputSettingsState());
					Camera.instance.shake(3);
				});

			}
		}.setSparks(true));

		Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");

				transition(() -> {
					if (fromGame) {
						Dungeon.goToLevel(Dungeon.depth);
					} else {
						Dungeon.game.setState(new MainMenuState());
					}

					Camera.instance.shake(3);
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