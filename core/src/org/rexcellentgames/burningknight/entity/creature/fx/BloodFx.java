package org.rexcellentgames.burningknight.entity.creature.fx;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BloodFx extends Entity {
	private Point vel;

	public static void add(Entity entity, int count) {
		add(entity.x, entity.y, entity.w, entity.h, count);
	}

	public static void add(float x, float y, float w, float h, int count) {
		if (!Settings.blood) {
			return;
		}

		for (int i = 0; i < count; i++) {
			BloodFx fx = new BloodFx();

			fx.x = Random.newFloat(w - 3.5f) + x + 3.5f;
			fx.y = Random.newFloat(h - 3.5f) + y + 3.5f;

			Dungeon.area.add(fx);
		}
	}

	@Override
	public void init() {
		this.vel = new Point(
			Random.newFloat(-1f, 1f),
			Random.newFloat(-1f)
		);

		this.r = Random.newFloat(0.7f, 1f);
		this.g = Random.newFloat(0f, 0.15f);
		this.b = Random.newFloat(0f, 0.15f);
		this.av = Random.newFloat(0.5f, 1f) * (Random.chance(50) ? -1 : 1);
	}

	private boolean second;
	private float av;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt * 60;
		this.y += this.vel.y * dt * 60;

		this.vel.x *= 0.98f;
		this.vel.y -= dt;

		this.angle += this.av * dt * 360;

		if (this.second) {
			this.size -= dt * 10;

			if (this.size <= 0) {
				this.done = true;
			}
		} else {
			this.size += dt * 48;

			if (this.size >= 5f) {
				this.size = 5f;
				this.second = true;
			}
		}
	}

	private float size;
	private float r;
	private float g;
	private float b;
	private float angle;

	@Override
	public void render() {
		Graphics.startShape();
		Graphics.shape.setColor(r, g, b, 1);

		float s = this.size / 2;

		Graphics.shape.rect(this.x, this.y, s, s, this.size,
			this.size, 1, 1, this.angle);

		Graphics.endShape();
	}
}