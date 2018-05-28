package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.trap.FourSideRotatingTurret;
import org.rexellentgames.dungeon.entity.trap.RotatingTurret;
import org.rexellentgames.dungeon.entity.trap.Turret;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class RotatingTurretRoom extends TrapRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		Turret turret = Random.chance(50) ? new FourSideRotatingTurret() : new RotatingTurret();

		turret.x = center.x * 16;
		turret.y = center.y * 16;

		Dungeon.area.add(turret);
		Dungeon.level.addSaveable(turret);
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	@Override
	public int getMinWidth() {
		return 8;
	}
}