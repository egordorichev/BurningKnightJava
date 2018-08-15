package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.Camera;
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

		first = (UiEntity) Dungeon.ui.add(new UiButton("bindings", (int) (Display.GAME_WIDTH * 1.5f), 128 + 24 - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				super.onClick();
				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.game.setState(new KeyConfigState());
						Camera.shake(3);
					}
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

		Dungeon.ui.add(new UiButton("back", (int) (Display.GAME_WIDTH * 1.5f), (int) (128 - 24 * 1.5f) - Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				Dungeon.ui.select(SettingsState.first);

				Tween.to(new Tween.Task(Display.GAME_HEIGHT * 0.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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