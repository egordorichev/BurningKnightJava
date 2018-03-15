package org.rexellentgames.dungeon.entity.level.rooms.connection;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class RingConnectionRoom extends TunnelRoom {
	@Override
	public int getMinWidth() {
		return Math.max(5, super.getMinWidth());
	}

	@Override
	public int getMinHeight() {
		return Math.max(5, super.getMinHeight());
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		super.paint(level);

		Rect ring = getConnectionSpace();

		Painter.fill(level, ring.left, ring.top, 3, 3, Terrain.FLOOR);
		Painter.fill(level, ring.left + 1, ring.top + 1, 1, 1, Terrain.WALL);
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