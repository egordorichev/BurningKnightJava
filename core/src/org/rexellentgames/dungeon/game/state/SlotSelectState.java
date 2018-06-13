package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.ui.UiButton;

public class SlotSelectState extends State {
	public static boolean added;

	public static void add() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 2.5f) - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");
			}
		});
	}
}