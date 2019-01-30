package org.rexcellentgames.burningknight.entity.level.rooms.boss;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.util.Random;

public class BossRoom extends EntranceRoom {
	protected boolean alwaysElipse;

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		int m = Random.newInt(2, 4);

		if (Random.chance(30)) {
			if (alwaysElipse || Random.chance(50)) {
				Painter.fillEllipse(level, this, 1, Random.chance(50) ? Terrain.LAVA : Terrain.CHASM);
				Painter.fillEllipse(level, this, m, Terrain.randomFloor());
			} else {
				Painter.fill(level, this, 1, Random.chance(50) ? Terrain.LAVA : Terrain.CHASM);
				Painter.fill(level, this, m, Terrain.randomFloor());
			}

			m += Random.newInt(1, 3);
			paintTunnels(level, true);
		} else {
			if (alwaysElipse || Random.chance(50)) {
				Painter.fillEllipse(level, this, 1, Terrain.randomFloor());
				paintTunnels(level, true);
			} else {
				Painter.fill(level, this, 1, Terrain.randomFloor());
			}
		}

		if (Random.chance(90)) {
			if (!alwaysElipse && Random.chance(50)) {
				Painter.fill(level, this, m, Terrain.randomFloor());
			} else {
				Painter.fillEllipse(level, this, m, Terrain.randomFloor());
			}

			paintTunnels(level, false);

			if (Random.chance(90)) {
				m += Random.newInt(1, 3);

				if (!alwaysElipse && Random.chance(50)) {
					Painter.fill(level, this, m, Terrain.randomFloor());
				} else {
					Painter.fillEllipse(level, this, m, Terrain.randomFloor());
				}
			}
		} else if (Random.chance(50)) {
			paintTunnels(level, false);
		}

		paintTunnels(level, true);

		if (Random.chance(50)) {
			int n = Math.min(getWidth() / 2, getHeight() / 2) - Random.newInt(2, 6);

			if (Random.chance(50)) {
				Painter.fill(level, this, n, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloorNotLast());
			} else {
				Painter.fillEllipse(level, this, n, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloorNotLast());
			}

			if (Random.chance(50)) {
				n += 1;

				if (Random.chance(50)) {
					Painter.fill(level, this, n, Terrain.randomFloorNotLast());
				} else {
					Painter.fillEllipse(level, this, n, Terrain.randomFloorNotLast());
				}
			}
		}

		for (Door door : connected.values()) {
			door.setType(Door.Type.BOSS);
		}
	}

	private void paintTunnels(Level level, boolean force) {
		if (Random.chance(50) || force) {
			if (Random.chance(50)) {
				paintTunnel(level, Terrain.randomFloor(), true);
				paintTunnel(level, Terrain.randomFloorNotLast());
			} else {
				paintTunnel(level, Terrain.randomFloor());
			}
		}
	}

	@Override
	public int getMinWidth() {
		return 18 + 5;
	}

	@Override
	public int getMinHeight() {
		return 18 + 5;
	}

	@Override
	public int getMaxWidth() {
		return 36;
	}

	@Override
	public int getMaxHeight() {
		return 36;
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}
}