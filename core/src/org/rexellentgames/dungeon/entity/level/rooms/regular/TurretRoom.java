package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.trap.Turret;

public class TurretRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int y = this.top + 2; y < this.bottom - 1; y += 2) {
			Turret turret = new Turret();

			turret.x = (this.left + 2) * 16;
			turret.y = y * 16;
			turret.a = (float) (Math.PI * 1.5f);

			Dungeon.area.add(turret);
			Dungeon.level.addSaveable(turret);
		}
	}
}