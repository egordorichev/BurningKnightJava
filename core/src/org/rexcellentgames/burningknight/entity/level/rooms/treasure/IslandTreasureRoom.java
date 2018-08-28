package org.rexcellentgames.burningknight.entity.level.rooms.treasure;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class IslandTreasureRoom extends TreasureRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.FLOOR_D);
		Painter.fill(level, this, 1, (Random.chance(50) ? Terrain.CHASM : Terrain.WALL));

		paintTunnel(level, Random.chance(30) ? Terrain.FLOOR_D : Terrain.randomFloor());

		Rect connetion = this.getConnectionSpace();

		Painter.fill(level, connetion.left, connetion.top, 3, 3, Random.chance(30) ? Terrain.FLOOR_D : Terrain.randomFloor());

		placeChest(getCenter());
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}

	private Rect connSpace;

	@Override
	protected Rect getConnectionSpace() {
		if (connSpace == null) {
			Point c = getDoorCenter();

			c.x = (int) MathUtils.clamp(left + 2, right - 2, c.x);
			c.y = (int) MathUtils.clamp(top + 2, bottom - 2, c.y);


			connSpace = new Rect((int) c.x - 1, (int) c.y - 1, (int) c.x + 1, (int) c.y + 1);
		}

		return connSpace;
	}
}