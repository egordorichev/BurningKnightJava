package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.ColorUtils;
import org.rexcellentgames.burningknight.util.Random;

public class Confetti extends Entity {
	private float r;
	private float g;
	private float b;
	private float t;
	public Vector2 vel = new Vector2();
	private float a;
	private float va;
	private float tt;
	private float z;
	private float m = 1f;

	{
		w = 8;
		h = 4;
		z = 20;
		depth = 1;
	}

	@Override
	public void init() {
		super.init();

		Color color = ColorUtils.HSV_to_RGB(Random.newFloat(360f), 100f, 100f);

		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.t = Random.newFloat(2);

		this.va = Random.newFloat(-1, 1) * 360f;
		this.a = Random.newFloat(360);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;
		this.tt += dt;
		this.a += this.va * dt;

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;
		this.z += this.vel.y * dt;

		this.vel.x *= 0.98f;

		if (this.z > 0) {
			this.vel.y -= 1f *dt * 60;
		} else {
			this.vel.x = 0;
			this.vel.y = 0;
			this.m = Math.max(0, this.m - dt * 5);
		}

		if (this.tt >= 5f || this.m == 0) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.startShape();

		// float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float degrees)
		Graphics.shape.setColor(this.r, this.g, this.b, 1);
		Graphics.shape.rect(this.x, this.y, this.w / 2, this.h / 2, this.w, this.h, (float) Math.cos(this.t * 1.5f) * this.m,  (float) Math.sin(this.t * 0.9f) * this.m, this.a * this.m);
		Graphics.endShape();
	}
}