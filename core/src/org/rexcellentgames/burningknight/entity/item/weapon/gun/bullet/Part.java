package org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Part extends Entity {
	public Point vel;
	public float speed = 1f;
	private float size;
	private float val;
	private boolean second;

	@Override
	public void init() {
		this.alwaysActive = true;

		if (this.vel == null) {
			this.vel = new Point(
				Random.newFloat(-1f, 1f),
				Random.newFloat(-1f, 1f)
			);
		}

		this.val = Random.newFloat(0.7f, 1f);
		this.tar = Random.newFloat(3, 5f);
		this.vel.mul(60);
	}

	private float tar;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.mul(0.95f);

		if (this.second) {
			this.size -= dt * speed * 2f;
		} else {
			this.size += dt * 40f;

			if (this.size >= this.tar) {
				this.size = this.tar;
				this.second = true;
			}
		}

		if (this.size <= 0) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.startShape();
		Graphics.shape.setColor(val, val, val, 1);
		Graphics.shape.circle(this.x, this.y, this.size);
		Graphics.shape.setColor(1, 1, 1, 1);
		Graphics.endShape();
	}
}