package org.rexcellentgames.burningknight.entity.trap;

import org.rexcellentgames.burningknight.util.Log;

public class FourSideRotatingTurret extends FourSideTurret {
	@Override
	protected void rotate() {
		this.a += Math.PI / 4;
		this.str = !this.str;
		this.tween();
		this.t = 0;
	}
}