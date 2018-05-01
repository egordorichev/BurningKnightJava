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
import org.rexellentgames.dungeon.util.Animation;

import java.util.ArrayList;

public class GraphicsSettingsState extends State {
	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("menu_label (quality)", Display.GAME_WIDTH / 2, 128 + 24 * 2) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiButton("menu_label (screenshake)", Display.GAME_WIDTH / 2, 128 + 24) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiCheckbox("menu_label (fullscreen)", Display.GAME_WIDTH / 2, 128) {
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

		Dungeon.area.add(new UiCheckbox("menu_label (blood)", Display.GAME_WIDTH / 2, 128 - 24) {
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

		Dungeon.area.add(new UiCheckbox("menu_label (gore)", Display.GAME_WIDTH / 2, 128 - 24 * 2) {
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

		Dungeon.area.add(new UiButton("menu_label (shaders)", Display.GAME_WIDTH / 2, 128 - 24 * 3) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiCheckbox("menu_label (vsync)", Display.GAME_WIDTH / 2, 128 + 24 * 3) {
			@Override
			public void onClick() {
				super.onClick();
				Camera.instance.shake(3);

				Settings.vsync = !Settings.vsync;
				Gdx.graphics.setVSync(Settings.vsync);
			}
		}.setOn(Settings.vsync).setSparks(true));
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}