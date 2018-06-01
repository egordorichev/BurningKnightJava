package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;

public class UiMap extends UiEntity {
	{
		depth = 16;
	}

	@Override
	public void init() {
		super.init();

		this.w = 64;
		this.h = this.w / 4f * 3f;

		this.x = Display.GAME_WIDTH - this.w - 4;
		this.y = Display.GAME_HEIGHT - this.h - 4;
	}

	@Override
	public void render() {
		Rectangle scissors = new Rectangle();
		Rectangle clipBounds = new Rectangle(x,y,w,h);
		ScissorStack.calculateScissors(Camera.ui, Graphics.batch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Graphics.startShape();
		Graphics.shape.setColor(0.5f, 0.5f, 0.5f, 1);
		Graphics.shape.rect(this.x, this.y, this.w, this.h);

		float zoom = Camera.instance.getCamera().zoom;

		float px = Player.instance.x + Player.instance.w / 2f;
		float py = Player.instance.y + Player.instance.h / 2f;

		int s = (int) (4 * zoom);

		float mx = -px / (16f / s) + this.x + this.w / 2;
		float my = -py / (16f / s) + this.y + this.h / 2;

		float o = 1f;

		int xx = 0;
		int yy;

		for (int x = 0; x < Level.getWidth(); x++) {
			yy = 0;

			for (int y = 0; y < Level.getHeight(); y++) {
				byte t = Dungeon.level.get(x, y);

				if (t != Terrain.WALL && t != Terrain.CRACK) {
					Graphics.shape.setColor(0, 0, 0, 0);
					Graphics.shape.rect(xx * s - o + mx, yy * s - o + my, s + o * 2, s + o * 2);
				}

				yy ++;
			}

			xx ++;
		}

		xx = 0;

		for (int x = 0; x < Level.getWidth(); x++) {
			yy = 0;

			for (int y = 0; y < Level.getHeight(); y++) {
				byte t = Dungeon.level.get(x, y);

				if (t != Terrain.WALL && t != Terrain.CRACK) {
					Graphics.shape.setColor(1, 1, 1, 1);
					Graphics.shape.rect(xx * s + mx, yy * s + my, s, s);
				}

				yy ++;
			}

			xx ++;
		}

		Graphics.endShape();

		ScissorStack.popScissors();
	}
}