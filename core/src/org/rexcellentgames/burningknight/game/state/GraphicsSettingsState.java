package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.*;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

public class GraphicsSettingsState extends State {	
	public static boolean added;
	public static UiEntity first;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		Dungeon.ui.add(new UiChoice("cursor", (int) (Display.UI_WIDTH_MAX * 1.5f), 138 + 20 * 2 + Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				super.onClick();
				Settings.cursor = Settings.cursors[this.getCurrent()];
				Settings.cursorId = this.getCurrent();
			}
		}.setChoices(Settings.cursors.clone()).setCurrent(Settings.getCursorId(Settings.cursor)));

		Dungeon.ui.add(new UiCheckbox("rotate_cursor", (int) (Display.UI_WIDTH_MAX * 1.5f), 138 + 20 + Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				super.onClick();

				Settings.rotateCursor = this.isOn();
			}
		}.setOn(Settings.rotateCursor));

		Dungeon.ui.add(new UiSlider("screenshake", (int) (Display.UI_WIDTH_MAX * 1.5f), 138 + Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/select");
			}

			@Override
			public void onUpdate() {
				Settings.screenshake = this.val;
			}
		}.setValue(Settings.screenshake));

		Dungeon.ui.add(new UiCheckbox("fullscreen", (int) (Display.UI_WIDTH_MAX * 1.5f), 138 - 20 + Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				super.onClick();

				Settings.fullscreen = this.isOn();

				if (Settings.fullscreen) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				} else {
					Gdx.graphics.setWindowedMode(Display.UI_WIDTH_MAX * 2, Display.UI_HEIGHT_MAX * 2);
				}
			}

			@Override
			public void update(float dt) {
				super.update(dt);
				setOn(Settings.fullscreen);
			}
		}.setOn(Settings.fullscreen).setSparks(true));

		Dungeon.ui.add(new UiCheckbox("blood", (int) (Display.UI_WIDTH_MAX * 1.5f), 138 - 20 * 2 + Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				this.setPlaySfx(Settings.blood);
				super.onClick();

				Settings.blood = !Settings.blood;


				if (Settings.blood) {
					Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4));
				}
			}
		}.setOn(Settings.blood));

		Dungeon.ui.add(new UiCheckbox("gore", (int) (Display.UI_WIDTH_MAX * 1.5f), 138 - 20 * 3 + Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				this.setPlaySfx(Settings.gore);
				super.onClick();

				Settings.gore = !Settings.gore;

				if (Settings.gore) {
					Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4));
					/*ArrayList<Animation.Frame> frames = Random.newFloat() > 0.3 ?
						(Random.newFloat() > 0.5 ? CrazyKing.animations.getFrames("dead") : Knight.animations.getFrames("death")) : Clown.animations.getFrames("death");

					for (Animation.Frame frame : frames) {
						GoreFx fx = new GoreFx();

						fx.texture = frame.frame;
						fx.x = this.x;
						fx.y = this.y;
						fx.menu = true;

						Dungeon.ui.add(fx);
					}*/
				}
			}
		}.setOn(Settings.gore));

		first = (UiEntity) Dungeon.ui.add(new UiCheckbox("vsync", (int) (Display.UI_WIDTH_MAX * 1.5f), 138 + 20 * 4 + Display.UI_HEIGHT_MAX) {
			@Override
			public void onClick() {
				super.onClick();

				Settings.vsync = !Settings.vsync;
				Gdx.graphics.setVSync(Settings.vsync);
			}
		}.setOn(Settings.vsync).setSparks(true));

		Dungeon.ui.add(new UiButton("back", (int) (Display.UI_WIDTH_MAX * 1.5f), (int) (138 - 20 * 5.5f) + Display.UI_HEIGHT_MAX) {
			@Override
			public void render() {
				super.render();

				if (SettingsState.current == SettingsState.Type.GRAPHICS && Input.instance.wasPressed("pause")) {
					Input.instance.putState("pause", Input.State.UP);
					this.onClick();
				}
			}

			@Override
			public void onClick() {
				Audio.playSfx("menu/exit");
				Dungeon.ui.select(SettingsState.first);

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
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}