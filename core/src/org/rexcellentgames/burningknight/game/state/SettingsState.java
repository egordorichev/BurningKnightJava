package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Tween;

public class SettingsState extends State {
	public static boolean added;
	public static UiEntity first;
	public static boolean toGame;

	public enum Type {
		SETTINGS, AUDIO, INPUT, GRAPHICS, CONFIG
	}

	public static Type current = Type.SETTINGS;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		first = (UiEntity) Dungeon.ui.add(new UiButton("graphics", (int) (Display.UI_WIDTH_MAX * 1.5f), 128 + 24) {
			@Override
			public void onClick() {
				super.onClick();
				GraphicsSettingsState.add();
				Dungeon.ui.select(GraphicsSettingsState.first);

				current = Type.GRAPHICS;

				Tween.to(new Tween.Task(Display.UI_HEIGHT_MAX * 1.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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

		Dungeon.ui.add(new UiButton("audio", (int) (Display.UI_WIDTH_MAX * 1.5f), 128) {
			@Override
			public void onClick() {
				super.onClick();
				AudioSettingsState.add();
				Dungeon.ui.select(AudioSettingsState.first);

				current = Type.AUDIO;

				Tween.to(new Tween.Task(Display.UI_WIDTH_MAX * 2.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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

		/*
		Dungeon.ui.add(new UiButton("input", (int) (Display.UI_WIDTH_MAX * 1.5f), 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();
				InputSettingsState.add();
				Dungeon.ui.select(InputSettingsState.first);

				current = Type.INPUT;

				Tween.to(new Tween.Task(-Display.UI_HEIGHT_MAX * 0.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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
		}.setSparks(true));*/

		Dungeon.ui.add(new UiButton("back", (int) (Display.UI_WIDTH_MAX * 1.5f), (int) (128 - 24 * 2.5f)) {
			@Override
			public void render() {
				super.render();

				if (current == Type.SETTINGS && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				SaveManager.save(SaveManager.Type.GLOBAL, false);

				if (toGame) {
					toGame = false;
					Dungeon.loadType = Entrance.LoadType.LOADING;

					transition(new Runnable() {
						@Override
						public void run() {
							Dungeon.goToLevel(Dungeon.depth);
						}
					});
				} else {
					Dungeon.ui.select(MainMenuState.first);

					Tween.to(new Tween.Task(Display.UI_WIDTH_MAX * 0.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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
			}
		});
	}
}