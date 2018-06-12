package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;

public class MainMenuState extends State {
	private static TextureRegion logo = Graphics.getTexture("artwork_logo (sticker)");

	@Override
	public void init() {
		Dungeon.area.add(Camera.instance);

		Dungeon.area.add(new UiButton("play", Display.GAME_WIDTH / 2, 128 - 24) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					Dungeon.goToLevel(Dungeon.depth);
					Camera.instance.shake(3);
				});
			}
		}.setSparks(true));

		Dungeon.area.add(new UiButton("settings", Display.GAME_WIDTH / 2, 128 - 24 * 2) {
			@Override
			public void onClick() {
				super.onClick();

				transition(() -> {
					SettingsState.fromGame = false;
					Dungeon.game.setState(new SettingsState());
					Camera.instance.shake(3);
				});
			}
		});

		Dungeon.area.add(new UiButton("exit", Display.GAME_WIDTH / 2, (int) (128 - 24 * 3.5f)) {
			@Override
			public void onClick() {
				Graphics.playSfx("menu/exit");

				transition(() -> Gdx.app.exit());
			}
		});
	}

	@Override
	public void render() {
		super.render();

		float sx = (float) (0.8f + Math.sin(Dungeon.time / 1.5f) / 40);
		float sy = (float) (0.8f + Math.cos(Dungeon.time) / 40);
		float a = (float) (Math.cos(Dungeon.time * 0.7f) * 3f);

		Graphics.render(logo, Display.GAME_WIDTH / 2, 180, a, logo.getRegionWidth() / 2, logo.getRegionHeight() / 2, false, false, sx, sy);
		Ui.ui.renderCursor();
	}
}