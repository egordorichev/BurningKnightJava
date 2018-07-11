package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;

public class UiMap extends UiEntity {
	{
		depth = 16;
	}

	private boolean large;
	private float xc;
	private float yc;

	@Override
	public void init() {
		super.init();

		setSize();
	}

	public void setSize() {
		this.w = large ? Display.GAME_WIDTH : 64;
		this.h = Math.min(this.w / 4f * 3f, Display.GAME_HEIGHT);

		this.x = Display.GAME_WIDTH - this.w - (large ? 0 : 4);
		this.y = Display.GAME_HEIGHT - this.h - (large ? 0 : 4);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		InGameState.map = large;

		if (large) {
			float s = 120f;

			if (Input.instance.isDown("left")) {
				xc += s * dt;
			}

			if (Input.instance.isDown("right")) {
				xc -= s * dt;
			}

			if (Input.instance.isDown("up")) {
				yc -= s * dt;
			}

			if (Input.instance.isDown("down")) {
				yc += s * dt;
			}

			float ix = -Input.instance.getAxis("mouseX") * s;
			float iy = Input.instance.getAxis("mouseY") * s;

			if (Math.sqrt(ix * ix + iy * iy) > 0.2) {
				xc += ix * dt;
				yc += iy * dt;
			}

		}

		if (Input.instance.wasPressed("map")) {
			large = !large;
			xc = 0;
			yc = 0;

			setSize();
		}
	}

	@Override
	public void render() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.end();

		if (!large) {
			Rectangle scissors = new Rectangle();
			Rectangle clipBounds = new Rectangle(x, y, w, h);
			ScissorStack.calculateScissors(Camera.ui, Graphics.batch.getTransformMatrix(), clipBounds, scissors);
			ScissorStack.pushScissors(scissors);
		}

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		if (large) {
			Graphics.shape.setColor(Dungeon.GRAY);
		} else {
			Graphics.shape.setColor(0.5f, 0.5f, 0.5f, 0.3f);
		}

		Graphics.shape.rect(this.x, this.y, this.w, this.h);

		float zoom = Camera.game.zoom;

		float px = Player.instance.x + Player.instance.w / 2f;
		float py = Player.instance.y + Player.instance.h / 2f;

		int s = (int) (large ? 6 : 4 * zoom);

		float mx = -px / (16f / s) + this.x + this.w / 2 + xc;
		float my = -py / (16f / s) + this.y + this.h / 2 + yc;

		float o = 1f;

		int xx = 0;
		int yy;


		for (int x = 0; x < Level.getWidth(); x++) {
			yy = 0;

			for (int y = 0; y < Level.getHeight(); y++) {
				if (Dungeon.level.explored(x, y)) {

					byte t = Dungeon.level.get(x, y);
					Graphics.shape.setColor(0, 0, 0, 1);
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
				if (Dungeon.level.explored(x, y)) {
					int i = Level.toIndex(x, y);
					byte t = Dungeon.level.liquidData[i];

					if (t == 0) {
						t = Dungeon.level.get(i);
					}

					Graphics.shape.setColor(Terrain.getColor(t));
					Graphics.shape.rect(xx * s + mx, yy * s + my, s, s);
				}

				yy ++;
			}

			xx ++;
		}

		float plx = Player.instance.x / 16f;
		float ply = Player.instance.y / 16f;

		Graphics.shape.setColor(0, 0, 0, 1);
		Graphics.shape.rect(plx * s + s / 4f - o + mx, ply * s + s / 4f - o + my, s / 2f + o * 2, s / 2f + o * 2);
		Graphics.shape.setColor(0, 1, 0, 1);
		Graphics.shape.rect(plx * s + s / 4f + mx, ply * s + s / 4f + my, s / 2f, s / 2f);

		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		if (!large) {
			try {
				ScissorStack.popScissors();
			} catch(IllegalStateException ignored) {

			}
		}

		Graphics.batch.begin();
	}
}