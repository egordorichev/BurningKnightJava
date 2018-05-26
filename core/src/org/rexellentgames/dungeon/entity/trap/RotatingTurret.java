package org.rexellentgames.dungeon.entity.trap;

import org.rexellentgames.dungeon.util.Random;

public class RotatingTurret extends Turret {
	private boolean left;

	@Override
	public void init() {
		super.init();

		sp = 1f;
		left = Random.chance(50);
	}

	@Override
	protected void send() {
		super.send();
		this.a += (left ? -Math.PI / 4 : Math.PI / 4);
	}
}