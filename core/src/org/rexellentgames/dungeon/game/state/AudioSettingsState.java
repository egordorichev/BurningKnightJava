package org.rexellentgames.dungeon.game.state;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.assets.Audio;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiCheckbox;
import org.rexellentgames.dungeon.ui.UiSlider;
import org.rexellentgames.dungeon.util.Tween;

public class AudioSettingsState extends State {
	public static boolean added;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		Dungeon.area.add(new UiSlider("music", (int) (Display.GAME_WIDTH * 2.5f), 128 + 24) {
			@Override
			public void onClick() {
				Camera.shake(3);
				Audio.playSfx("menu/select");
			}

			@Override
			public void onUpdate() {
				Settings.music = this.val;
				Audio.update();
			}
		}.setValue(Settings.music));

		Dungeon.area.add(new UiSlider("sfx", (int) (Display.GAME_WIDTH * 2.5f), 128) {
			@Override
			public void onClick() {
				Camera.shake(3);
				Audio.playSfx("menu/select");
			}

			@Override
			public void onUpdate() {
				Settings.sfx = this.val;
			}
		}.setValue(Settings.sfx));

		Dungeon.area.add(new UiCheckbox("uisfx", (int) (Display.GAME_WIDTH * 2.5f), 128 - 24) {
			@Override
			public void onClick() {
				Camera.shake(3);
				Settings.uisfx = !Settings.uisfx;
				super.onClick();
			}
		}.setOn(Settings.uisfx));

		Dungeon.area.add(new UiButton("back", (int) (Display.GAME_WIDTH * 2.5f), (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");

				Tween.to(new Tween.Task(Display.GAME_WIDTH * 1.5f, MainMenuState.MOVE_T) {
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