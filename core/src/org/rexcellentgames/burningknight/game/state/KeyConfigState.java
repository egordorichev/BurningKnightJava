package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;

public class KeyConfigState extends State {
	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("reset_to_default", Display.GAME_WIDTH / 2, (int) (128 - 24 * 4.5f)) {
			@Override
			public void onClick() {
				super.onClick();

				Camera.shake(3);
			}
		});

		Dungeon.area.add(new UiButton("save", Display.GAME_WIDTH / 2, (int) (128 - 24 * 3.5f)) {
			@Override
			public void onClick() {
				transition(() -> {
					Audio.playSfx("menu/exit");
					Dungeon.game.setState(new InputSettingsState());
					Camera.shake(3);
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