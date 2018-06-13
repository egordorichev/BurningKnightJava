package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Audio;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.util.Tween;

public class SlotSelectState extends State {
	public static boolean added;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 2.5f) - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(Display.GAME_HEIGHT / 2, 0.4f) {
					@Override
					public float getValue() {
						return MainMenuState.cameraY;
					}

					@Override
					public void setValue(float value) {
						MainMenuState.cameraY = value;
					}
				});
			}
		});
	}
}