package org.rexcellentgames.burningknight.game.fx;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class BackgroundFlameFx extends Entity {
	{
		alwaysActive = true;
		depth = 1;
	}

	private float g;
	private float t;
	private float tt;
	private float size = 1f;
	private float range = 1;
	private float angle;
	private float s;

	public float oy;
	private float speed;
	private float ox;

	@Override
	public void init() {
		speed = Random.newFloat(0.7f, 2f);
		y = -32 - Random.newFloat(32f);
		x = Random.newFloat(0, Display.GAME_WIDTH);

		g = Random.newFloat(1);
		oy = y;
		ox = x;
		done = false;
		t = Random.newFloat(1024);
		tt = 0;
		size = Random.newFloat(8, 16);
		s = Random.newFloat(3, 6);
		range = Random.newFloat(32, 64);
	}

	@Override
	public void update(float dt) {
		this.t += dt;
		this.tt += dt;
		this.y = this.oy + this.tt * 48 * speed;

		this.size -= dt * 4 * speed;

		if (this.size <= 0) {
			this.init();
		}

		this.x = ox + (float) (Math.cos(this.t) * this.range);
		this.angle = (float) Math.cos(this.t * this.s) * 20;
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();

		float s = this.size / 2;

		Graphics.shape.setColor(1, g, 0, 0.3f);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 2, 2, this.angle);

		Graphics.shape.setColor(1, g, 0, 0.7f);
		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, this.angle);

		Graphics.endAlphaShape();
	}
}