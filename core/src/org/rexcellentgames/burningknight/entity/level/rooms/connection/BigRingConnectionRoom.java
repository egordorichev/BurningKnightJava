package org.rexcellentgames.burningknight.entity.level.rooms.connection;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class BigRingConnectionRoom extends TunnelRoom {
	@Override
	public int getMinWidth() {
		return Math.max(7, super.getMinWidth());
	}

	@Override
	public int getMinHeight() {
		return Math.max(7, super.getMinHeight());
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);

		if (Random.chance(50)) {
			Painter.fill(level, this, 1, Terrain.CHASM);
		}

		Rect ring = getConnectionSpace();
		byte floor = Terrain.randomFloor();

		Painter.fill(level, ring.left - 1, ring.top - 1, 5, 5, floor);

		if (Random.chance(50)) {
			Painter.fill(level, ring.left + 1, ring.top + 1, 1, 1, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);

			if (Random.chance(50)) {
				Painter.fill(level, ring.left, ring.top, 3, 3, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
			}
		} else {
			Painter.fill(level, ring.left, ring.top, 3, 3, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
		}

		paintTunnel(level, Terrain.randomFloor());

		Painter.fill(level, ring.left, ring.top, 3, 3, Terrain.randomFloor());
		Painter.fill(level, ring.left + 1, ring.top + 1, 1, 1, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);

		for (Door d : connected.values()) {
			d.setType(Door.Type.TUNNEL);
		}
	}

	private Rect connSpace;

	@Override
	protected Rect getConnectionSpace() {
		if (connSpace == null) {
			Point c = getDoorCenter();

			c.x = (int) MathUtils.clamp(left + 3, right - 3, c.x);
			c.y = (int) MathUtils.clamp(top + 3, bottom - 3, c.y);

			connSpace = new Rect((int) c.x - 1, (int) c.y - 1, (int) c.x + 1, (int) c.y + 1);
		}

		return connSpace;
	}
}