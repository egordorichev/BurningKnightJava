package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.GoreFx;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Clown;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.Knight;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.CrazyKing;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiCheckbox;
import org.rexcellentgames.burningknight.ui.UiChoice;
import org.rexcellentgames.burningknight.ui.UiSlider;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class GraphicsSettingsState extends State {	
	public static boolean added;

	public static void add() {
		if (added) {
			return;
		}

		added = true;

		Dungeon.area.add(new UiChoice("quality", (int) (Display.GAME_WIDTH * 1.5f), 138 + 20 * 2 + Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				super.onClick();

				Camera.shake(3);

				switch (this.getCurrent()) {
					case 0: default: Settings.quality = 2; break;
					case 1: Settings.quality = 4; break;
					case 2: Settings.quality = 1; break;
				}

				org.rexcellentgames.burningknight.assets.Graphics.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}
		}.setChoices(new String[] {
			"normal",
			"good",
			"bad"
		}).setCurrent(
			(Settings.quality == 2 ? 0 : (Settings.quality == 4 ? 1 : 2))
		));

		Dungeon.area.add(new UiSlider("screenshake", (int) (Display.GAME_WIDTH * 1.5f), 138 + 20 + Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				Audio.playSfx("menu/select");
				Camera.shake(3);
			}

			@Override
			public void onUpdate() {
				Settings.screenshake = this.val;
			}
		}.setValue(Settings.screenshake));

		Dungeon.area.add(new UiCheckbox("fullscreen", (int) (Display.GAME_WIDTH * 1.5f), 138 + Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.shake(3);

				Settings.fullscreen = !Settings.fullscreen;

				if (Settings.fullscreen) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				} else {
					Gdx.graphics.setWindowedMode(Display.GAME_WIDTH * 2, Display.GAME_HEIGHT * 2);
				}
			}
		}.setOn(Settings.fullscreen).setSparks(true));

		Dungeon.area.add(new UiCheckbox("blood", (int) (Display.GAME_WIDTH * 1.5f), 138 - 20 + Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				this.setPlaySfx(Settings.blood);
				super.onClick();

				Settings.blood = !Settings.blood;
				Camera.shake(3);

				if (Settings.blood) {
					Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4));
					BloodFx.add(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h, 10);
				}
			}
		}.setOn(Settings.blood));

		Dungeon.area.add(new UiCheckbox("gore", (int) (Display.GAME_WIDTH * 1.5f), 138 - 20 * 2 + Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				this.setPlaySfx(Settings.gore);
				super.onClick();

				Settings.gore = !Settings.gore;
				Camera.shake(3);

				if (Settings.gore) {
					Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4));
					ArrayList<Animation.Frame> frames = Random.newFloat() > 0.3 ?
						(Random.newFloat() > 0.5 ? CrazyKing.animations.getFrames("dead") : Knight.animations.getFrames("death")) : Clown.animations.getFrames("death");

					for (Animation.Frame frame : frames) {
						GoreFx fx = new GoreFx();

						fx.texture = frame.frame;
						fx.x = this.x;
						fx.y = this.y;
						fx.menu = true;

						Dungeon.area.add(fx);
					}
				}
			}
		}.setOn(Settings.gore));

		Dungeon.area.add(new UiCheckbox("vsync", (int) (Display.GAME_WIDTH * 1.5f), 138 + 20 * 3 + Display.GAME_HEIGHT) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.shake(3);

				Settings.vsync = !Settings.vsync;
				Gdx.graphics.setVSync(Settings.vsync);
			}
		}.setOn(Settings.vsync).setSparks(true));

		Dungeon.area.add(new UiButton("back", (int) (Display.GAME_WIDTH * 1.5f), (int) (138 - 20 * 4.5f) + Display.GAME_HEIGHT) {
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
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}