package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.input.Input;

public class Ui {
	public static Ui ui;

	private TextureRegion cursor;
	private TextureRegion topFrame;

	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui (cursor)");
		topFrame = Graphics.getTexture("ui (top frame)");
	}

	public void render() {
		if (Camera.ui != null) {
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		}

		Graphics.render(this.topFrame, 0, Display.GAME_HEIGHT - topFrame.getRegionHeight());

		// Cursor

		float s = (float) (Math.cos(Dungeon.time * 2) * 2) + 16;

		Graphics.render(this.cursor, Input.instance.uiMouse.x - 8,
			Input.instance.uiMouse.y - 8,
			Dungeon.time * 60, s / 2, s / 2, false, false);
	}
}