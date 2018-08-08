package org.rexcellentgames.burningknight.entity.level.rooms.connection;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

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

		if (Random.chance(50)) {
			Painter.fill(level, this, 1, Terrain.CHASM);
		}

		byte fl = (Random.chance(25) ? (Random.chance(33) ? Terrain.CHASM : (Random.chance(50) ? Terrain.WALL : Terrain.LAVA))
				: Terrain.randomFloor());

		if (this.getWidth() > 4 && this.getHeight() > 4 && Random.chance(50)) {
			this.paintTunnel(level, fl, true);
		}

		if (fl == Terrain.LAVA) {
			this.paintTunnel(level, Terrain.randomFloor());
		}

		this.paintTunnel(level, (fl == Terrain.DIRT || fl == Terrain.LAVA) ? (Random.chance(50) ? Terrain.WATER : Terrain.DIRT) : Terrain.randomFloor());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.TUNNEL);
		}

		Rect ring = getConnectionSpace();
		byte floor = Terrain.randomFloor();

		Painter.fill(level, ring.left, ring.top, 3, 3, Terrain.randomFloor());
		Painter.fill(level, ring.left, ring.top, 3, 3, fl == Terrain.LAVA ? (Random.chance(50) ? Terrain.WATER : Terrain.DIRT) : floor);
		Painter.fill(level, ring.left + 1, ring.top + 1, 1, 1, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
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