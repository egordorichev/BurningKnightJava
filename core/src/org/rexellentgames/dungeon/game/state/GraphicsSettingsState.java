package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.ui.UiCheckbox;

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
		}.setSparks(true));

		Dungeon.area.add(new UiButton("menu_label (blood)", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiButton("menu_label (gore)", Display.GAME_WIDTH / 2, 128 - 24 * 2) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
			}
		});

		Dungeon.area.add(new UiButton("menu_label (shaders)", Display.GAME_WIDTH / 2, 128 - 24 * 3) {
			@Override
			public void onClick() {
				Camera.instance.shake(3);
			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		Ui.ui.renderCursor();
	}
}