package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.trap.Turret;
import org.rexcellentgames.burningknight.util.Random;

public class TurretRoom extends TrapRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, Random.chance(50) ? Terrain.FLOOR_A : Terrain.FLOOR_B);

		if (Random.chance(50)) {
			Painter.fillEllipse(level, this, Random.chance(50) ? Terrain.FLOOR_A : Terrain.FLOOR_B);
		}

		boolean wave = Random.chance(50);

		int s = Random.newInt(2, 4);
		float a = Random.newFloat(1, 3);

		if (Random.chance(50)) {
			boolean left = Random.chance(50);
			int x = (left ? (this.left + 2) : (this.right - 2)) * 16;
			float i = 0;

			for (int y = this.top + 2; y < this.bottom - 1; y += s) {
				Turret turret = new Turret();

				turret.x = x;
				turret.y = y * 16;

				if (wave) {
					turret.last = i / a % 3;
					i++;
				}

				turret.a = (float) (!left ? Math.PI : 0);

				Dungeon.area.add(turret);
				LevelSave.add(turret);
			}
		} else {
			boolean top = Random.chance(50);
			int y = (top ? (this.top + 2) : (this.bottom - 2)) * 16;
			float i = 0;

			for (int x = this.left + 2; x < this.right - 1; x += s) {
				Turret turret = new Turret();

				turret.x = x * 16;
				turret.y = y;

				if (wave) {
					turret.last = i / a % 3;
					i++;
				}

				turret.a = (float) (!top ? Math.PI * 1.5f : Math.PI / 2);

				Dungeon.area.add(turret);
				LevelSave.add(turret);
			}
		}
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMinWidth() {
		return 7;
	}
}