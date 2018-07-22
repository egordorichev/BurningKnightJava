package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class ParticleSpawner extends Entity {
	private Vector2 vel = new Vector2();
	private float max;

	public ParticleSpawner(float x, float y) {
		this.x = x;
		this.y = y;
		this.alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();

		this.max = Random.newFloat(1f, 1.5f);

		double a = Random.newFloat(360);
		float speed = 60 * Random.newFloat(0.9f, 1.2f);
		this.vel.x = (float) (Math.cos(a) * speed);
		this.vel.y = (float) (Math.sin(a) * speed);
	}

	private float t;
	private float last;
	private int i;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.last += dt;
		if (this.last >= 0.05f) {
			this.last = 0;

			TinyParticle explosion = new TinyParticle(x, y);
			explosion.depth = 50 + this.i;
			Dungeon.area.add(explosion);

			this.i ++;
		}

		this.t += dt;

		if (this.t >= this.max) {
			this.done = true;
		}

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.y -= 60f * dt;
	}
}