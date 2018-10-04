package org.rexcellentgames.burningknight.entity.fx;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.Random;

public class BloodDropFx extends Entity {
	public Creature owner;
	private float a;
	private float tsz;
	private float sz;
	private boolean second;
	private float rtSpd;
	private float xv;
	private float yv;

	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 1;
	}

	private float g;
	private float b;
	private float r;

	@Override
	public void init() {
		super.init();

		this.r = Random.newFloat(0.7f, 1f);
		this.g = Random.newFloat(0f, 0.15f);
		this.b = Random.newFloat(0f, 0.15f);

		this.tsz = Random.newFloat(4, 6);
		this.a = Random.newFloat(360);
		this.rtSpd = Random.newFloat(45, 180) * (Random.chance(50) ? -1 : 1);
		this.xv = Random.newFloat(-1, 1) * 10;

		this.x = this.owner.w / 2 - 4 + Random.newFloat(8) - this.tsz / 2;
		this.y = this.owner.h - Random.newFloat(this.owner.h / 3) - 6;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.a += this.rtSpd;
		this.yv += dt * 90;
		this.y -= this.yv * dt;
		this.x += this.xv * dt;

		if (this.second) {
			this.sz -= dt * 10;

			if (this.sz <= 0) {
				this.done = true;
			}
		} else {
			this.sz += (this.tsz - this.sz) * dt * 20;
			if (this.tsz - this.sz <= 0.4f) {
				this.second = true;
				this.sz = this.tsz;
			}
		}
	}

	@Override
	public void render() {
		Graphics.startShape();
		Graphics.shape.setColor(r, g, b, 1);

		Graphics.shape.rect(this.x + this.owner.x, this.y + this.owner.y + this.owner.z, this.sz / 2, this.sz / 2, this.sz, this.sz, 1, 1, this.a);
		Graphics.endShape();
	}
}