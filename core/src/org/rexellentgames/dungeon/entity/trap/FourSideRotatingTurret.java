package org.rexellentgames.dungeon.entity.trap;

import org.rexellentgames.dungeon.util.Random;

public class FourSideRotatingTurret extends FourSideTurret {
	private boolean left;

	@Override
	public void init() {
		super.init();
		left = Random.chance(50);
	}

	@Override
	protected void send() {
		super.send();
		this.a += (left ? -Math.PI / 4 : Math.PI / 4);
	}
}