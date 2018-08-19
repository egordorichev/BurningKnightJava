package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.ui.UiKey;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Tween;

public class KeyConfigState extends State {
	public static boolean added;
	public static UiEntity first;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		Log.info("add");

		float w = Display.GAME_WIDTH / 4;
		float y = 32;

		Dungeon.ui.add(new UiKey("up", (int) (Display.GAME_WIDTH * 1.5f - w), (int) (128 + 24 * 3.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("down", (int) (Display.GAME_WIDTH * 1.5f - w), (int) (128 + 24 * 2.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("left", (int) (Display.GAME_WIDTH * 1.5f - w), (int) (128 + 24 * 1.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("right", (int) (Display.GAME_WIDTH * 1.5f - w), (int) (128 + 24 * 0.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("toggle_minimap", (int) (Display.GAME_WIDTH * 1.5f - w), (int) (128 + 24 * -0.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("map", (int) (Display.GAME_WIDTH * 1.5f - w), (int) (128 + 24 * -1.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		// Second row

		Dungeon.ui.add(new UiKey("use", (int) (Display.GAME_WIDTH * 1.5f + w), (int) (128 + 24 * 3.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("interact", (int) (Display.GAME_WIDTH * 1.5f + w), (int) (128 + 24 * 2.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("drop", (int) (Display.GAME_WIDTH * 1.5f + w), (int) (128 + 24 * 1.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("inventory", (int) (Display.GAME_WIDTH * 1.5f + w), (int) (128 + 24 * 0.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("pause", (int) (Display.GAME_WIDTH * 1.5f + w), (int) (128 + 24 * -0.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("zoom_in", (int) (Display.GAME_WIDTH * 1.5f + w), (int) (128 + 24 * -1.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiKey("zoom_out", (int) (Display.GAME_WIDTH * 1.5f + w), (int) (128 + 24 * -2.5f - Display.GAME_HEIGHT * 2f + y)) {

		});

		Dungeon.ui.add(new UiButton("reset_to_default", (int) (Display.GAME_WIDTH * 1.5f), (int) (128 + 24 * 12.5f - Display.GAME_HEIGHT * 3.5f)) {
			@Override
			public void onClick() {
				super.onClick();
				Input.instance.resetBindings();
			}
		});

		first = (UiEntity) Dungeon.ui.add(new UiButton("save", (int) (Display.GAME_WIDTH * 1.5f), (int) (128 + 24 * 11.5f - Display.GAME_HEIGHT * 3.5f)) {
			@Override
			public void onClick() {
				Input.instance.saveBindings();

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
		});

		/*
		 * Important keys:
		 * + Movement (WASD)
		 * + Drop
		 * + Use
		 * + Interact
		 * + Inventory open
		 * + Pause
		 * + Toggle map
		 * + Big map
		 * + Minimap zoom (+ / -)
		 */
	}

	@Override
	public void renderUi() {
		super.renderUi();
		Ui.ui.renderCursor();
	}
}