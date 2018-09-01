package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class SteamFx extends Entity {
	{
		t = Random.newFloat(1024);
		tt = 0;
		s = Random.newFloat(3, 6);
		alwaysActive = true;
	}

	private float t;
	private float tt;
	private float size = 1f;
	private float angle;
	private float s;
	private boolean second;

	private float oy;
	public float val;
	private float speed;
	private float al = 1;

	@Override
	public void init() {
		super.init();
		oy = y;
		speed = Random.newFloat(1f, 2f);
		val = Random.newFloat(0.7f, 1f);
	}

	@Override
	public void update(float dt) {
		this.t += dt;
		this.tt += dt;
		this.y = this.oy + this.tt * 15 * speed;

		if (this.second) {
			this.al -= dt * 0.5;

			if (this.al <= 0) {
				this.done = true;
			}
		} else {
			this.size += dt * 80;
			if (this.size >= 4) {
				this.size = 4;
				this.second = true;
			}
		}

		this.angle = (float) Math.cos(this.t * this.s) * 20;

		if (this.y - oy > 64) {
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

		Graphics.shape.setColor(val, val, val, al);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, this.angle);

		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();
	}
}