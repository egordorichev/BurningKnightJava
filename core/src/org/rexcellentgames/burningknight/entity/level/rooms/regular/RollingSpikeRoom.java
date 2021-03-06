package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RollingSpikeRoom extends TrapRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		int i = 0;
		int sp = Random.newInt(2, 4);

		if (Random.chance(50)) {
			for (int y = this.top + 2; y < this.bottom - 1; y += sp) {
				RollingSpike spike = new RollingSpike();

				spike.x = ((i % 2 == 0) ? this.left + 2 : this.right - 2) * 16;
				spike.y = y * 16;
				float s = 20f;
				spike.velocity = new Point(i % 2 == 0 ? s : -s, 0);

				Dungeon.area.add(spike);
				LevelSave.add(spike);

				i++;
			}
		} else {
			for (int x = this.left + 2; x < this.right - 1; x += sp) {
				RollingSpike spike = new RollingSpike();

				spike.y = ((i % 2 == 0) ? this.top + 2 : this.bottom - 2) * 16;
				spike.x = x * 16;
				float s = 20f;
				spike.velocity = new Point(0, i % 2 == 0 ? s : -s);

				Dungeon.area.add(spike);
				LevelSave.add(spike);

				i++;
			}
		}
	}

	@Override
	public int getMinWidth() {
		return 7;
	}

	@Override
	public int getMinHeight() {
		return 7;
	}
}