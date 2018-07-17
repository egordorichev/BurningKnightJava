package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiClass;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class ClassSelectState extends State {
	public static boolean added;
	public static ArrayList<UiClass> classes = new ArrayList<>();
	public static float add;
	public static UiClass selectedClass;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		for (Player.Type type : Player.Type.values()) {
			classes.add(((UiClass) Dungeon.ui.add(new UiClass(type, Display.GAME_WIDTH / 4, (int) -(Display.GAME_HEIGHT * 1.5f)) {

			})));
		}

		Dungeon.ui.add(new UiButton("play", Display.GAME_WIDTH / 2 + 128, (int) (128 - 24 * 3.5f) - Display.GAME_HEIGHT * 2) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = selectedClass.type;

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.goToLevel(0);
					}
				});
			}
		});

		Dungeon.ui.add(new UiButton("back", Display.GAME_WIDTH / 2 + 64, (int) (128 - 24 * 3.5f) - Display.GAME_HEIGHT * 2) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(-Display.GAME_HEIGHT / 2, MainMenuState.MOVE_T) {
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