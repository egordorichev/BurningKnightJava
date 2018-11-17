package org.rexcellentgames.burningknight.entity.fx;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class FadeFx extends Entity {
	private float a;
	private float al;
	private float r;
	private float g;
	private float b;
	private float size;
	private float aVel;
	public Point vel;
	public boolean to;
	private boolean grow;
	private float target;

	{
		alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();

		if (to) {
			grow = true;
			target = Random.newFloat(8, 16);
		} else {
			size = Random.newFloat(3, 5);
		}

		al = 1;
		r = 2;
		g = Random.newFloat(0.6f, 1f) + 0.5f;
		b = 1f;
		a = Random.newFloat((float) (Math.PI * 2));
		aVel = Random.newFloat(-1f, 1f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (r > 0.3f) {
			r -= dt * 1.5f;
			g -= dt * 1.5f;
			b -= dt * 1.5f;
		}

		a += aVel * dt * 320;
		aVel *= (dt * 58);

		x += vel.x * dt;
		y += vel.y * dt;
		vel.x *= (dt * 58);
		vel.y *= (dt * 58);

		if (to) {
			if (grow) {
				size += dt * 120;

				if (size >= target) {
					size = target;
					grow = false;
				}
			} else {
				size -= dt * 10;
			}
		} else {
			size += dt * 10;
		}

		al -= dt * 0.7;

		if (al <= 0 || size <= 0) {
			done = true;
		}
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();
		Graphics.shape.setColor(r, g, b, al);
		// rect(float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float degrees)
		Graphics.shape.rect(x, y, size / 2, size / 2, size, size, 1, 1, a);
		Graphics.endAlphaShape();
	}
}