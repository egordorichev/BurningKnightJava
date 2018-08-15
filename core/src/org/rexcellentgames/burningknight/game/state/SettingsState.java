package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Tween;

public class SettingsState extends State {
	public static boolean added;
	public static UiEntity first;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		first = (UiEntity) Dungeon.ui.add(new UiButton("graphics", (int) (Display.GAME_WIDTH * 1.5f), 128 + 24) {
			@Override
			public void onClick() {
				super.onClick();
				GraphicsSettingsState.add();
				Dungeon.ui.select(GraphicsSettingsState.first);

				Tween.to(new Tween.Task(Display.GAME_HEIGHT * 1.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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
		}.setSparks(true));

		Dungeon.ui.add(new UiButton("audio", (int) (Display.GAME_WIDTH * 1.5f), 128) {
			@Override
			public void onClick() {
				super.onClick();
				AudioSettingsState.add();
				Dungeon.ui.select(AudioSettingsState.first);

				Tween.to(new Tween.Task(Display.GAME_WIDTH * 2.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return MainMenuState.cameraX;
					}

					@Override
					public void setValue(float value) {
						MainMenuState.cameraX = value;
					}
				});
			}
		}.setSparks(true));

		Dungeon.ui.add(new UiButton("input", (int) (Display.GAME_WIDTH * 1.5f), 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();
				InputSettingsState.add();
				Dungeon.ui.select(InputSettingsState.first);

				Tween.to(new Tween.Task(-Display.GAME_HEIGHT * 0.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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
		}.setSparks(true));

		Dungeon.ui.add(new UiButton("back", (int) (Display.GAME_WIDTH * 1.5f), (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				Dungeon.ui.select(MainMenuState.first);

				Tween.to(new Tween.Task(Display.GAME_WIDTH * 0.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
					@Override
					public float getValue() {
						return MainMenuState.cameraX;
					}

					@Override
					public void setValue(float value) {
						MainMenuState.cameraX = value;
					}
				});
			}
		});
	}
}