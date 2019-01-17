package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class GrassBreakFx extends Entity {
	{
		alwaysActive = true;
	}

	private float size;
	private float tarSize;
	private boolean second;
	private Vector2 vel = new Vector2();
	private float g;
	private float angle;

	@Override
	public void init() {
		super.init();

		g = Random.newFloat(0.7f, 1f);
		tarSize = Random.newFloat(3f, 6f);
		angle = Random.newFloat(360);

		vel.x = Random.newFloat(-1f, 1f) * 40;
		vel.y = Random.newFloat(-1f, 1f) * 40;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.x -= this.vel.x * Math.min(1, dt * 3);
		this.vel.y -= this.vel.y * Math.min(1, dt * 3);

		if (this.second) {
			this.size -= dt * 3f;

			if (this.size <= 0) {
				this.done = true;
			}
		} else {
			this.size += dt * 48;

			if (this.size >= this.tarSize) {
				this.size = this.tarSize;
				this.second = true;
			}
		}
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();
		float s = this.size;
		Graphics.shape.setColor(0.1f, g, 0.1f, 0.4f);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, this.angle);
		Graphics.endAlphaShape();
	}
}