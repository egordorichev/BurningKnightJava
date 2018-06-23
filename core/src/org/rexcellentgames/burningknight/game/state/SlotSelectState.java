package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiCard;
import org.rexcellentgames.burningknight.util.Tween;

public class SlotSelectState extends State {
	public static boolean added;

	public static void trans(final int l) {
		transition(new Runnable() {
			@Override
			public void run() {
				Dungeon.goToLevel(l);
			}
		});
	}

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		Dungeon.area.add(new UiCard(0, Display.GAME_WIDTH / 2 - 96 - 16, (int) (128+ 24 * 0.5f) - Display.GAME_HEIGHT) {

		});

		Dungeon.area.add(new UiCard(1, Display.GAME_WIDTH / 2, (int) (128+ 24 * 0.5f) - Display.GAME_HEIGHT) {

		});

		Dungeon.area.add(new UiCard(2, Display.GAME_WIDTH / 2 + 96 + 16, (int) (128+ 24 * 0.5f) - Display.GAME_HEIGHT) {

		});

		Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 3.5f) - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(Display.GAME_HEIGHT / 2, MainMenuState.MOVE_T) {
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