package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.trap.FourSideRotatingTurret;
import org.rexcellentgames.burningknight.entity.trap.RotatingTurret;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.trap.FourSideRotatingTurret;
import org.rexcellentgames.burningknight.entity.trap.RotatingTurret;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RotatingTurretRoom extends TrapRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		Turret turret = Random.chance(50) ? new FourSideRotatingTurret() : new RotatingTurret();

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