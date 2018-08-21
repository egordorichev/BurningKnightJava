package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BattleRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		int m = Random.newInt(this.getWidth() / 2 - 5, this.getWidth() / 2 - 3);
		Painter.fill(level, this, m, Terrain.CHASM);

		Painter.fill(level, this, m + Random.newInt(1, 3), Terrain.randomFloor());
		paintTunnel(level, Terrain.FLOOR_D);
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}

	@Override
	public int getMinHeight() {
		return 12;
	}

	@Override
	public int getMinWidth() {
		return 12;
	}

	@Override
	public int getMaxHeight() {
		return 22;
	}

	@Override
	public int getMaxWidth() {
		return 22;
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 16;
		}

		return 4;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}
}