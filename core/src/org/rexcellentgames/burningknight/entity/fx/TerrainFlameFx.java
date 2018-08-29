package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class TerrainFlameFx extends Entity {
	{
		t = Random.newFloat(1024);
		tt = 0;
		s = Random.newFloat(3, 6);
		alwaysActive = true;
	}

	private float g;
	private float t;
	private float tt;
	private float size = 1f;
	private float range = 1;
	private float angle;
	private float s;
	private boolean second;

	private float oy;
	private float ox;

	@Override
	public void init() {
		super.init();
		g = Random.newFloat(1);
		oy = y;
		ox = x;
	}

	@Override
	public void update(float dt) {
		this.t += dt;
		this.tt += dt;
		this.y = this.oy + this.tt * 15;

		if (this.second) {
			this.size -= dt * 4;

			if (this.size <= 0) {
				this.done = true;
			}
		} else {
			this.size += dt * 80;
			if (this.size >= 4) {
				this.size = 4;
				this.second = true;
			}
		}

		if (this.range < 6) {
			this.range += dt * 15;
		}

		this.x = ox + (float) (Math.cos(this.t) * this.range);
		this.angle = (float) Math.cos(this.t * this.s) * 20;

		if (this.y - oy > 16) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.setProjectionMatrix(Camera.game.combined);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		float s = this.size / 2;


		Graphics.shape.setColor(1, g, 0, 0.3f);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 2, 2, this.angle);

		Graphics.shape.setColor(1, g, 0, 0.7f);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, this.angle);

		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();
	}
}