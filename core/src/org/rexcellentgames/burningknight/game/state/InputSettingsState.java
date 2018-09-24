package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiChoice;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class InputSettingsState extends State {
	private static UiChoice c;
	public static boolean added;
	public static UiEntity first;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		first = (UiEntity) Dungeon.ui.add(new UiButton("bindings", (int) (Display.UI_WIDTH_MAX * 1.5f), 128 + 24 - Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				super.onClick();

				KeyConfigState.add();
				Dungeon.ui.select(KeyConfigState.first);

				SettingsState.current = SettingsState.Type.CONFIG;

				Tween.to(new Tween.Task(-Display.UI_HEIGHT_MAX * 1.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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

		c = new UiChoice("joystick", (int) (Display.UI_WIDTH_MAX * 1.5f), 128 - Display.UI_HEIGHT_MAX) {
			@Override
			public void onUpdate() {
				super.onUpdate();

				if (label.endsWith("None")) {
					Input.instance.activeController = null;
					return;
				}

				for (Controller controller : Controllers.getControllers()) {
					if (label.endsWith(controller.getName().replaceAll("\\s+"," "))) {
						Input.instance.activeController = controller;
						Input.instance.onControllerChange();
						break;
					}
				}
			}
		};

		updateChoices();

		Dungeon.ui.add(c);

		Dungeon.ui.add(new UiButton("back", (int) (Display.UI_WIDTH_MAX * 1.5f), (int) (128 - 24 * 1.5f) - Display.UI_HEIGHT_MAX) {
			@Override
			public void render() {
				super.render();

				if (SettingsState.current == SettingsState.Type.INPUT && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				Dungeon.ui.select(SettingsState.first);
				Input.listener = null;

				SettingsState.current = SettingsState.Type.SETTINGS;

				Tween.to(new Tween.Task(Display.UI_HEIGHT_MAX * 0.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.hasControllerChanged()) {
			updateChoices();
		}
	}

	private static void updateChoices() {
		ArrayList<String> options = new ArrayList<>();

		options.add("None");
		int i = 1;
		int s = -1;

		for (Controller controller : Controllers.getControllers()) {
			String name = controller.getName();

			if ((!name.contains("gamepad") && !name.contains("joy") && !name.contains("stick") && !name.contains("controller")) || name.contains("wacom")) {
				Log.info("Controller " + controller.getName() + " was ignored");
				continue;
			}

			options.add(controller.getName().replaceAll("\\s+"," "));

			if (controller == Input.instance.activeController || Input.instance.activeController == null) {
				s = i;
				Input.instance.activeController = controller;

				Log.info("set to " + controller.getName());
			}

			i++;
		}

		c.setChoices(options.toArray(new String[] {}));

		if (s != -1) {
			c.setCurrent(s);
		}
	}
}