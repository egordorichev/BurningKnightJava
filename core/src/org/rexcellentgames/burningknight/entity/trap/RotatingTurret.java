package org.rexcellentgames.burningknight.entity.trap;

import org.rexcellentgames.burningknight.util.Random;

public class RotatingTurret extends Turret {
	private boolean left;

	@Override
	public void init() {
		super.init();

		sp = 1f;
		rotates = true;
		left = Random.chance(50);
	}

	@Override
	protected void send() {
		this.a += (left ? -Math.PI / 4 : Math.PI / 4);
		super.send();
	}
}