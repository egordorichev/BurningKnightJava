package org.rexcellentgames.burningknight.entity.fx;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class CurseFx extends Entity {
	private float val;
	private float angle;
	private float size;
	private boolean second;
	private float av;
	private float al = 1;
	private float yv;
	private float speed;
	private float tarVal;

	{
		alwaysActive = true;
		alwaysRender = true;
	}

	@Override
	public void init() {
		super.init();

		this.angle = Random.newFloat(360f);
		this.av = Random.newFloat(0.5f, 1f) * (Random.chance(50) ? -1 : 1);
		this.tarVal = Random.newFloat(0, 0.3f);
		this.speed = Random.newFloat(0.5f, 1f);
		this.val = Random.newFloat(0.9f, 1f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.angle += this.av * 10;
		this.yv += dt * 360 * 0.5f;
		this.y += this.yv * dt * speed;

		if (this.second) {
			this.al -= dt;

			if (this.al <= 0) {
				this.done = true;
			}
		} else {
			this.size += dt * 25f;

			if (this.size >= 5f) {
				this.second = true;
				this.size = 5f;
			}
		}

		this.val += (this.tarVal - this.val) * dt * 3;
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();

		float s = this.size / 2;

		Graphics.shape.setColor(val, val, val, this.al);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, this.angle);

		Graphics.endAlphaShape();
	}
}