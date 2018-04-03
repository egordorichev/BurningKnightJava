package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;

public class Ui {
	public static Ui ui;

	private TextureRegion cursor;
	private TextureRegion topFrame;
	private TextureRegion bar;

	public Ui() {
		ui = this;
		cursor = Graphics.getTexture("ui (cursor)");
		topFrame = Graphics.getTexture("ui (top frame)");
		bar = Graphics.getTexture("bk_health");
	}

	public void render() {
		if (BurningKnight.instance != null && BurningKnight.instance.target != null) {
			bar.getRegionWidth(BurningKnight.)
			Graphics.render(bar, 0, 0, 0, 0, 0, false, false);
		}

		if (Player.instance != null && Player.instance.isDead()) {
			Graphics.print("Game over!", Graphics.medium, 128);
			Graphics.print("Press space to restart", Graphics.medium, (float) (108 + Math.sin(Dungeon.time * 3) * 4));

			if (Input.instance.wasPressed("action")) {
				Dungeon.newGame();
			}
		}

		// Cursor

		float s = (float) (Math.cos(Dungeon.time * 2) * 2) + 16;

		Graphics.render(this.cursor, Input.instance.uiMouse.x - 8,
			Input.instance.uiMouse.y - 8,
			Dungeon.time * 60, s / 2, s / 2, false, false);
	}

	public void renderUi() {
		Graphics.render(this.topFrame, 0, Display.GAME_HEIGHT - topFrame.getRegionHeight());
	}
}