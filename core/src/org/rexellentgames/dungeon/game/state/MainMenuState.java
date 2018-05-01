package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;

public class MainMenuState extends State {
	@Override
	public void init() {
		Dungeon.area.add(new UiButton("Single Player", -1, 128) {
			@Override
			public void onClick() {

			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}