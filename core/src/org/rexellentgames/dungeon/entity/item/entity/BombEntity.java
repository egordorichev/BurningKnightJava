package org.rexellentgames.dungeon.entity.item.entity;

import org.rexellentgames.dungeon.entity.Entity;

public class BombEntity extends Entity {
	private float t;

	public BombEntity(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update(float dt) {
		this.t += dt;

		if (dt >= 5f) {
			this.done = true;
			// todo: fx
		}
	}

	@Override
	public void render() {
		// Graphics.render(Graphics.items, 6, this.x, this.y);
	}
}