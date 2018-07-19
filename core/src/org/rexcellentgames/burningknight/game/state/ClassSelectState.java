package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiClass;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Tween;

public class ClassSelectState extends State {
	public static boolean added;
	public static float add;
	public static UiEntity first;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		first = (UiEntity) Dungeon.ui.add(new UiClass(Player.Type.WARRIOR, Display.GAME_WIDTH / 2 - 96 - 16, (int) (128+ 24 * 0.5f) - Display.GAME_HEIGHT * 2) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = type;

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.goToLevel(0);
					}
				});
			}
		});

		Dungeon.ui.add(new UiClass(Player.Type.WIZARD, Display.GAME_WIDTH / 2, (int) (128+ 24 * 0.5f) - Display.GAME_HEIGHT * 2) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = type;

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.goToLevel(0);
					}
				});
			}
		});

		Dungeon.ui.add(new UiClass(Player.Type.RANGER, Display.GAME_WIDTH / 2 + 96 + 16, (int) (128+ 24 * 0.5f) - Display.GAME_HEIGHT * 2) {
			@Override
			public void onClick() {
				super.onClick();
				Player.toSet = type;

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.goToLevel(0);
					}
				});
			}
		});

		Dungeon.ui.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 3.5f) - Display.GAME_HEIGHT * 2) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				Dungeon.ui.select(SlotSelectState.first);

				Tween.to(new Tween.Task(-Display.GAME_HEIGHT * 0.5f, MainMenuState.MOVE_T) {
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