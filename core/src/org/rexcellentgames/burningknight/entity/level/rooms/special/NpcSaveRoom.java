package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.npc.Trader;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class NpcSaveRoom extends SpecialRoom {
	public static final String[] saveOrder = {
		"b", "d", "a", "c", "e", "g", "h"
	};

	private boolean alwaysElipse;

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		int m = Random.newInt(2, 4) + 1;

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

		/*
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
		}*/

		byte floor = Terrain.randomFloor();
		byte fl = Random.chance(50) ? Terrain.WALL : Terrain.CHASM;

		Painter.fillEllipse(level, this, 2, fl);
		Painter.fillEllipse(level, this, 3, floor);

		if (Random.chance(50)) {
			Painter.fill(level, this, 4, Terrain.randomFloorNotLast());
		} else {
			Painter.fillEllipse(level, this, 4, Terrain.randomFloorNotLast());
		}

		if (Random.chance(50)) {
			if (Random.chance(50)) {
				Painter.fill(level, this, 5, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloorNotLast());
			} else {
				Painter.fillEllipse(level, this, 5, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloorNotLast());
			}
		}

		byte f = floor;

		boolean s = false;

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), f);
			s = true;
		}

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), f);
			s = true;
		}

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), f);
			s = true;
		}

		if (Random.chance(50) || !s) {
			Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), f);
		}

		paintTunnels(level, true);

		Point center = getCenter();
		Trader trader = new Trader();

		trader.x = center.x * 16;
		trader.y = center.y * 16;

		for (String id : saveOrder) {
			if (GlobalSave.isFalse("npc_" + id + "_saved")) {
				trader.id = id;
				break;
			}
		}

		Log.error("Adding npc");

		Dungeon.area.add(trader.add());
		Dungeon.level.itemsToSpawn.add(new KeyC());

		for (Door door : connected.values()) {
			door.setType(Door.Type.LOCKED);
		}
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
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
	protected int validateWidth(int w) {
		return w % 2 == 0 ? w : w + 1;
	}

	@Override
	protected int validateHeight(int h) {
		return h % 2 == 0 ? h : h + 1;
	}

	@Override
	public int getMinWidth() {
		return 10;
	}

	@Override
	public int getMinHeight() {
		return 10;
	}
}