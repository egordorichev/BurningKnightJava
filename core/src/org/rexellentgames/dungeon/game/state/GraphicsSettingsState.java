package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.fx.GoreFx;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiCheckbox;
import org.rexellentgames.dungeon.ui.UiChoice;
import org.rexellentgames.dungeon.ui.UiSlider;
import org.rexellentgames.dungeon.util.Animation;

import java.util.ArrayList;

public class GraphicsSettingsState extends State {
	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiChoice("menu_label (quality_normal)", Display.GAME_WIDTH / 2, 138 + 20 * 2) {
			@Override
			public void onClick() {
				super.onClick();

				Camera.instance.shake(3);

				switch (this.getCurrent()) {
					case 0: Settings.quality = 2;
					case 1: Settings.quality = 4;
					case 2: Settings.quality = 1;
				}

				org.rexellentgames.dungeon.assets.Graphics.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}
		}.setChoices(new String[] {
			"menu_label (quality_normal)",
			"menu_label (quality_good)",
			"menu_label (quality_bad)"
		}).setCurrent(
			(Settings.quality == 2 ? 0 : (Settings.quality == 4 ? 1 : 2))
		));

		Dungeon.area.add(new UiSlider("menu_label (screenshake)", Display.GAME_WIDTH / 2, 138 + 20) {
			@Override
			public void onClick() {
				org.rexellentgames.dungeon.assets.Graphics.playSfx("menu/select");
				Camera.instance.shake(3);
			}

			@Override
			public void onUpdate() {
				Settings.screenshake = this.val;
			}
		}.setValue(Settings.screenshake));

		Dungeon.area.add(new UiCheckbox("menu_label (fullscreen)", Display.GAME_WIDTH / 2, 138) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.instance.shake(3);

				Settings.fullscreen = !Settings.fullscreen;

				if (Settings.fullscreen) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				} else {
					Gdx.graphics.setWindowedMode(Display.GAME_WIDTH * 2, Display.GAME_HEIGHT * 2);
				}
			}
		}.setOn(Settings.fullscreen).setSparks(true));

		Dungeon.area.add(new UiCheckbox("menu_label (blood)", Display.GAME_WIDTH / 2, 138 - 20) {
			@Override
			public void onClick() {
				super.onClick();

				Settings.blood = !Settings.blood;
				Camera.instance.shake(3);

				if (Settings.blood) {
					BloodFx.add(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h, 10);
				}
			}
		}.setOn(Settings.blood));

		Dungeon.area.add(new UiCheckbox("menu_label (gore)", Display.GAME_WIDTH / 2, 138 - 20 * 2) {
			@Override
			public void onClick() {
				super.onClick();

				Settings.gore = !Settings.gore;
				Camera.instance.shake(3);

				if (Settings.gore) {
					ArrayList<Animation.Frame> frames = Knight.animations.getFrames("dead");

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

		Dungeon.area.add(new UiCheckbox("menu_label (shaders)", Display.GAME_WIDTH / 2, 138 - 20 * 3) {
			@Override
			public void onClick() {
				super.onClick();

				Camera.instance.shake(3);
				Settings.shaders = !Settings.shaders;

				if (Settings.shaders) {
					Dungeon.addCrt();
				} else {
					Dungeon.postProcessor.removeEffect(Dungeon.crt);
				}
			}
		}.setOn(Settings.shaders).setSparks(true));

		Dungeon.area.add(new UiCheckbox("menu_label (vsync)", Display.GAME_WIDTH / 2, 138 + 20 * 3) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.instance.shake(3);

				Settings.vsync = !Settings.vsync;
				Gdx.graphics.setVSync(Settings.vsync);
			}
		}.setOn(Settings.vsync).setSparks(true));

		Dungeon.area.add(new UiButton("menu_label (back)", Display.GAME_WIDTH / 2, (int) (138 - 20 * 4.5f)) {
			@Override
			public void onClick() {
				org.rexellentgames.dungeon.assets.Graphics.playSfx("menu/exit");
				Dungeon.game.setState(new SettingsState());
				Camera.instance.shake(3);

				Settings.save();
			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}