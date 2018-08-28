package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class FireFx extends Entity {
	private static TextureRegion region = Graphics.getTexture("particle-big");

	private float r;
	private float g;
	private float scale;
	private float a;
	private float t;
	private float av;
	public Point vel = new Point();
	private boolean second;
	private float last;
	private float b;

	{
		depth = 1;
	}

	private float al = 0.9f;
	private float min;

	@Override
	public void init() {
		super.init();
		this.t = Random.newFloat(0.3f);

		this.min = Random.newFloat(0.1f, 0.2f);
		this.g = 1f;
		this.r = 1f;
		this.b = 1f;
		this.a = Random.newFloat(360);
		this.av = Random.newFloat(0.5f, 1f) * (Random.chance(50) ? -1 : 1);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.last += dt;

		if (this.last >= 0.2f) {
			this.last = 0;
			Dungeon.level.setOnFire(Level.toIndex(Math.round(this.x / 16), Math.round(this.y / 16)), true);
		}

		this.b = Math.max(0f, b - dt * 2f);
		this.g = Math.max(this.min, g - dt);

		this.t += dt;
		this.scale += this.scale >= 0.8f ? this.scale * dt * 0.5f : dt * 5;

		if (this.second) {
			this.al -= dt;

			if (this.al <= 0f) {
				this.done = true;
			}
		} else {
			if (this.scale >= 1f) {
				this.second = true;
			}
		}

		this.a += this.av * dt * 360 * 3;
		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.mul(0.985f);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(r, g, b, this.al);
		Graphics.render(region, this.x, this.y, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, scale, scale);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}