package org.rexellentgames.dungeon.entity.item.pet.impl;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.geometry.Point;

public class SimpleFollowPet extends PetEntity {
	protected float maxDistance = 32f;
	protected Entity target;
	protected boolean dependOnDistance;
	protected boolean buildPath;
	protected Point next;

	@Override
	public void init() {
		super.init();

		this.target = this.owner;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.buildPath) {

		} else {
			float dx = this.target.x + this.target.w / 2 - this.x - this.w / 2;
			float dy = this.target.y + this.target.h / 2 - this.y - this.h / 2;
			double d = Math.sqrt(dx * dx + dy * dy);

			if (d > maxDistance) {
				if (dependOnDistance) {
					d *= 0.25f;

					this.vel.x += dx / d;
					this.vel.y += dy / d;
				} else {
					float s = 10f;

					this.vel.x += dx / s;
					this.vel.y += dy / s;
				}
			}
		}

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.x *= 0.9f;
		this.vel.y *= 0.9f;
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h, this.z);
	}
}