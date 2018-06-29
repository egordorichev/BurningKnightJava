package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class FourSideTurretRoom extends TrapRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		/*Turret turret = new FourSideTurret();

		turret.x = center.x * 16;
		turret.y = center.y * 16;

		Dungeon.area.add(turret);
		LevelSave.add(turret);*/
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