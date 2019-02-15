package org.rexcellentgames.burningknight.entity.creature.fx;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;

public class Zzz extends Entity {
	{
		alwaysActive = true;
	}

	private float sx;
	private float sy;
	private float t;
	public float delay;

	@Override
	public void init() {
		super.init();
		sx = x;
		sy = y;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (delay > 0) {
			delay -= dt;
			if (delay > 0) {
				return;
			}
		}

		t += dt;
		x = (float) (sx + Math.cos(t * 6.5f) * Math.max(0, 4 - t * 1.5f) * 1.5f);
		y = sy + t * 16;

		if (t >= 3f) {
			done = true;
		}
	}

	@Override
	public void render() {
		if (delay > 0) {
			return;
		}

		Graphics.small.setColor(1, 1, 1, (3 - t) * 0.33f);
		Graphics.print("z", Graphics.small, this.x, this.y);
		Graphics.small.setColor(1, 1, 1, 1);
	}
}