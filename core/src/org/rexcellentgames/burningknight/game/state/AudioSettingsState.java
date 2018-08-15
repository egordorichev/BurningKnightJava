package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiCheckbox;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.ui.UiSlider;
import org.rexcellentgames.burningknight.util.Tween;

public class AudioSettingsState extends State {
	public static boolean added;
	public static UiEntity first;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		first = (UiEntity) Dungeon.ui.add(new UiSlider("music", (int) (Display.GAME_WIDTH * 2.5f), 128 + 24) {
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

		Dungeon.ui.add(new UiSlider("sfx", (int) (Display.GAME_WIDTH * 2.5f), 128) {
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

		Dungeon.ui.add(new UiCheckbox("uisfx", (int) (Display.GAME_WIDTH * 2.5f), 128 - 24) {
			@Override
			public void onClick() {
				Camera.shake(3);
				Settings.uisfx = !Settings.uisfx;
				super.onClick();
			}
		}.setOn(Settings.uisfx));

		Dungeon.ui.add(new UiButton("back", (int) (Display.GAME_WIDTH * 2.5f), (int) (128 - 24 * 2.5f)) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				Dungeon.ui.select(SettingsState.first);

				Tween.to(new Tween.Task(Display.GAME_WIDTH * 1.5f, MainMenuState.MOVE_T, Tween.Type.QUAD_IN_OUT) {
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