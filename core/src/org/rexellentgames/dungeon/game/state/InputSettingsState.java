package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;

public class InputSettingsState extends State {
	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("bindings", Display.GAME_WIDTH / 2, 128 + 24) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiButton("joystick", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 1.5f)) {
			@Override
			public void onClick() {
				transition(new Runnable() {
					@Override
					public void run() {
						Graphics.playSfx("menu/exit");
						Dungeon.game.setState(new SettingsState());
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