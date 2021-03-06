package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.trap.FourSideTurret;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class FourSideTurretRoom extends TrapRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Random.chance(50) ? Terrain.FLOOR_A : Terrain.FLOOR_B);

		if (Random.chance(50)) {
			Painter.fillEllipse(level, this, 1, Random.chance(50) ? Terrain.FLOOR_A : Terrain.FLOOR_B);
		}

		Point center = this.getCenter();
		Turret turret = new FourSideTurret();

		turret.x = center.x * 16;
		turret.y = center.y * 16;

		Dungeon.area.add(turret);
		LevelSave.add(turret);
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