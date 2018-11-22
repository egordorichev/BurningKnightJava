package org.rexcellentgames.burningknight.entity.fx;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class WaterfallFx extends Entity {
	private float size;
	private float al;
	private float vel;
	private float an;
	private float anVel;
	private float tar;
	private boolean grow = true;
	private float r;
	private float g;
	private float b;
	public boolean lava;

	{
		alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();

		size = 1;
		al = Random.newFloat(0.8f, 1f);
		tar = Random.newFloat(3, 6) * 1.2f;
		an = Random.newFloat(360f);
		anVel = Random.newFloat(-1, 1) * 120;
		vel = 0;

		if (lava) {
			g = Random.newFloat(0.4f, 0.6f);
			r = Random.newFloat(0.8f, 1.2f);
		} else {
			r = 1;
			g = Random.newFloat(1.4f, 1.8f);
			b = Random.newFloat(g, 2f);
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (grow) {
			size += dt * 40;

			if (size >= tar) {
				size = tar;
				grow = false;
			}
		} else {
			size -= dt * 3;
		}

		al -= dt * 0.5f;
		vel -= dt * 20;
		y += dt * vel;
		an += anVel * dt;

		if (b > 0.5f) {
			if (lava) {
				r -= dt * 1;
			} else {
				r -= dt * 1.5f;
				b -= dt * 1f;
			}

			g -= dt * 1f;
		}

		if (al < 0 || size < 0) {
			done = true;
		}
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();
		Graphics.shape.setColor(r, g, b, al * 0.5f);
		Graphics.shape.rect(x - size / 2, y - size / 2, size / 2, size / 2, size, size, 1, 1, an);
		Graphics.shape.setColor(r, g, b, al);
		Graphics.shape.rect(x - size / 2, y - size / 2, size / 2, size / 2, size, size, 0.5f, 0.5f, an);
		Graphics.endAlphaShape();
	}
}