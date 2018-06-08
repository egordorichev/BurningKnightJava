package org.rexellentgames.dungeon.entity.trap;

public class FourSideRotatingTurret extends FourSideTurret {
	@Override
	protected void send() {
		this.a += Math.PI / 4;
		this.str = !this.str;
		super.send();
	}
}