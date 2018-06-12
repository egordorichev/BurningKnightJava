package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;

public class KeyConfigState extends State {
	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("reset_to_default", Display.GAME_WIDTH / 2, (int) (128 - 24 * 4.5f)) {
			@Override
			public void onClick() {
				super.onClick();

				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiButton("save", Display.GAME_WIDTH / 2, (int) (128 - 24 * 3.5f)) {
			@Override
			public void onClick() {
				transition(() -> {
					Graphics.playSfx("menu/exit");
					Dungeon.game.setState(new InputSettingsState());
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