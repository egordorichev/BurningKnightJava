package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.trap.RollingSpike;
import org.rexellentgames.dungeon.util.geometry.Point;

public class RollingSpikeRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		int i = 0;

		for (int y = this.top + 2; y < this.bottom - 2; y += 2) {
			RollingSpike spike = new RollingSpike();

			spike.x = ((i % 2 == 0) ? this.left + 2 : this.right - 2) * 16;
			spike.y = y * 16;
			float s = 30f;
			spike.vel = new Point(i % 2 == 0 ? s : -s, 0);

			Dungeon.area.add(spike);
			Dungeon.level.addSaveable(spike);

			i ++;
		}
	}
}