package org.rexellentgames.dungeon.entity.item.pet.impl;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;

public class SimpleFollowPet extends PetEntity {
	private float maxDistance = 32f;

	@Override
	public void update(float dt) {
		super.update(dt);

		float dx = this.owner.x + this.owner.w / 2 - this.x - this.w / 2;
		float dy = this.owner.y + this.owner.h / 2 - this.y - this.h / 2;
		double d = Math.sqrt(dx * dx + dy * dy);

		if (d > maxDistance) {
			float s = 10f;

			this.vel.x += dx / s;
			this.vel.y += dy / s;
		}

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.x *= 0.9f;
		this.vel.y *= 0.9f;
	}

	@Override
	public void render() {
		Graphics.render(Item.missing, this.x, this.y);
	}
}