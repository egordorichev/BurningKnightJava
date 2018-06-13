package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Audio;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiClass;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class ClassSelectState extends State {
	public static boolean added;
	public static ArrayList<UiClass> classes = new ArrayList<>();
	public static float add;
	public static UiClass selected;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		for (Player.Type type : Player.Type.values()) {
			classes.add(((UiClass) Dungeon.area.add(new UiClass(type, Display.GAME_WIDTH / 4, (int) -(Display.GAME_HEIGHT * 1.5f)) {

			})));
		}

		Dungeon.area.add(new UiButton("play", Display.GAME_WIDTH / 2 + 128, (int) (128 - 24 * 3.5f) - Display.GAME_HEIGHT * 2) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = selected.type;

				transition(() -> {
					Dungeon.goToLevel(0);
				});
			}
		});

		Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2 + 64, (int) (128 - 24 * 3.5f) - Display.GAME_HEIGHT * 2) {
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