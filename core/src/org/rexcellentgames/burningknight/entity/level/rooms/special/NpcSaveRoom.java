package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.npc.Trader;
import org.rexcellentgames.burningknight.entity.item.key.KeyA;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class NpcSaveRoom extends SpecialRoom {
	public static final String[] saveOrder = {
		"d", "a", "c", "e", "g", "h"
	};

	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			Painter.fillEllipse(level, this, Random.newInt(1, 3), Terrain.randomFloor());
		}

		Painter.fill(level, this, 3, Terrain.WALL);
		Painter.fill(level, this, 4, Terrain.randomFloor());
		Painter.fill(level, this, Random.newInt(5, 6), Terrain.randomFloor());

		Point point = new Point(getWidth() / 2 + this.left, this.top + 3);

		Door door = new Door((int) point.x, (int) point.y, false);

		door.lock = true;
		door.key = KeyA.class;
		door.lockable = true;

		Dungeon.area.add(door.add());

		Painter.set(level, point, Terrain.FLOOR_D);

		if (Random.chance(50)) {
			Painter.fillEllipse(level, this, Random.newInt(4, 6), Terrain.randomFloor());
		}

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

		Dungeon.area.add(trader.add());
		Dungeon.level.itemsToSpawn.add(new KeyA());
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