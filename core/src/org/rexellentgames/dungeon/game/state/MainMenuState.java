package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.ui.UiButton;
import org.rexellentgames.dungeon.util.Tween;

public class MainMenuState extends State {
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

		Ui.ui.renderCursor();
	}
}