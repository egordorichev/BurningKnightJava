package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiChoice;
import org.rexellentgames.dungeon.util.Log;

import java.util.ArrayList;

public class InputSettingsState extends State {
	private UiChoice c;

	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("bindings", Display.GAME_WIDTH / 2, 128 + 24) {
			@Override
			public void onClick() {
				super.onClick();
				transition(() -> {
					Dungeon.game.setState(new KeyConfigState());
					Camera.shake(3);
				});
			}
		});

		c = new UiChoice("joystick", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.shake(3);
			}

			@Override
			public void onUpdate() {
				super.onUpdate();

				if (this.label.endsWith("None")) {
					Input.instance.active = null;
					return;
				}

				for (Controller controller : Controllers.getControllers()) {
					if (this.label.endsWith(controller.getName().replaceAll("\\s+"," "))) {
						Input.instance.active = controller;
						break;
					}
				}
			}
		};

		this.updateChoices();

		Dungeon.area.add(c);

		Dungeon.area.add(new UiButton("back", Display.GAME_WIDTH / 2, (int) (128 - 24 * 1.5f)) {
			@Override
			public void onClick() {
				transition(() -> {
					Graphics.playSfx("menu/exit");
					Dungeon.game.setState(new SettingsState());
					Camera.shake(3);
				});
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.hasControllerChanged()) {
			this.updateChoices();
		}
	}

	private void updateChoices() {
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

		this.c.setChoices(options.toArray(new String[] {}));

		if (s != -1) {
			this.c.setCurrent(s);
		}
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}