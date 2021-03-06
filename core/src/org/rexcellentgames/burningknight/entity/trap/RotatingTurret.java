package org.rexcellentgames.burningknight.entity.trap;

public class RotatingTurret extends Turret {
	@Override
	protected void rotate() {
		this.a += Math.PI / 4;
		this.frame -= 1;
		this.tween();
	}

	@Override
	protected void setFrame() {
		this.single.setFrame(Math.floorMod(frame, 8));
	}
}