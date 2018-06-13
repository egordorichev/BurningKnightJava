package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Audio;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiChoice;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

import java.util.ArrayList;

public class InputSettingsState extends State {
	private static UiChoice c;
	public static boolean added;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		Dungeon.area.add(new UiButton("bindings", (int) (Display.GAME_WIDTH * 1.5f), 128 + 24 - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				super.onClick();
				transition(() -> {
					Dungeon.game.setState(new KeyConfigState());
					Camera.shake(3);
				});
			}
		});

		c = new UiChoice("joystick", (int) (Display.GAME_WIDTH * 1.5f), 128 - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.shake(3);
			}

			@Override
			public void onUpdate() {
				super.onUpdate();

				if (label.endsWith("None")) {
					Input.instance.active = null;
					return;
				}

				for (Controller controller : Controllers.getControllers()) {
					if (label.endsWith(controller.getName().replaceAll("\\s+"," "))) {
						Input.instance.active = controller;
						break;
					}
				}
			}
		};

		updateChoices();

		Dungeon.area.add(c);

		Dungeon.area.add(new UiButton("back", (int) (Display.GAME_WIDTH * 1.5f), (int) (128 - 24 * 1.5f) - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				
				Tween.to(new Tween.Task(Display.GAME_HEIGHT * 0.5f, MainMenuState.MOVE_T) {
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
			options.add(controller.getName().replaceAll("\\s+"," "));

			if (controller == Input.instance.active || Input.instance.active == null) {
				s = i;
				Input.instance.active = controller;

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