package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.util.Tween;

public class MainMenuState extends State {
	private static TextureRegion logo = Graphics.getTexture("logo");

	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("menu_label (play)", Display.GAME_WIDTH / 2, 128) {
			@Override
			public void onClick() {
				super.onClick();

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.goToLevel(Dungeon.depth);
						Camera.instance.shake(3);
					}
				});
			}
		}.setSparks(true));

		Dungeon.area.add(new UiButton("menu_label (settings)", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();

				transition(new Runnable() {
					@Override
					public void run() {
						Dungeon.game.setState(new SettingsState());
						Camera.instance.shake(3);
					}
				});
			}
		});

		Dungeon.area.add(new UiButton("menu_label (exit)", Display.GAME_WIDTH / 2, 128 - 24 * 2) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");

				transition(new Runnable() {
					@Override
					public void run() {
						Gdx.app.exit();
					}
				});
			}
		});
	}

	@Override
	public void renderUi() {
		super.renderUi();

		float sx = (float) (1f + Math.sin(Dungeon.time / 1.5f) / 5);
		float sy = (float) (1f + Math.cos(Dungeon.time) / 5);
		float a = (float) (Math.cos(Dungeon.time * 4) * 3f);

		Graphics.batch.setColor(UiButton.outline.r, UiButton.outline.g, UiButton.outline.b, 1);

		for (int xx = -1; xx < 2; xx++) {
			for (int yy = -1; yy < 2; yy++) {
				Graphics.render(logo, Display.GAME_WIDTH / 2 + xx, 200 + yy,
					a,
					logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, sx, sy);
			}
		}

		Graphics.batch.setColor(1, 1, 1, 1);

		Graphics.render(logo, Display.GAME_WIDTH / 2, 200, a, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, sx, sy);

		Ui.ui.renderCursor();
	}
}