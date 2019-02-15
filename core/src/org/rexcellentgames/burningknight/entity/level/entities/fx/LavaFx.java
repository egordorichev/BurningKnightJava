package org.rexcellentgames.burningknight.entity.level.entities.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class LavaFx extends Entity {
	private float size;
	private float a;
	private float vx;
	private float vy;
	private float rate;
	private float v;

	{
		alwaysActive = true;
	}

	public LavaFx(float x, float y) {
		this.x = x;
		this.y = y;
		this.v = Random.newFloat(0.7f, 1f);
		this.vx = Random.newFloat(-3f, 3f) * 2;
		this.vy = Random.newFloat(2.5f, 3f) * 2;

		this.size = Random.newFloat(2f, 4f);
		this.rate = 1 / this.size;

		m = Random.newFloat(0, 0.8f);
	}

	private float m;
	private boolean grow = true;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (grow) {
			this.a += dt * 4;

			if (this.a >= 1f) {
				this.a = 1f;
				this.grow = false;
			}
		} else {
			this.size -= dt / 2f * this.rate;
			this.a -= dt / 2f;

			if (this.a <= 0 || this.size <= 0) {
				this.done = true;
				return;
			}
		}

		this.y += dt * this.vy;
		this.x -= dt * this.vx;
	}

	@Override
	public void render() {
		Graphics.batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		float s = this.size / 2;

		Graphics.shape.setColor(v * 0.5f, 0, 0, this.a);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, 0);

		Graphics.shape.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();
	}
}