package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class TreasureRoom extends LockedRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.FLOOR_D);

		byte f = Random.chance(50) ? Terrain.WALL : Terrain.CHASM;

		Painter.set(level, new Point(this.left + 1, this.top + 1), f);
		Painter.set(level, new Point(this.right - 1, this.top + 1), f);
		Painter.set(level, new Point(this.left + 1, this.bottom - 1), f);
		Painter.set(level, new Point(this.right - 1, this.bottom - 1), f);

		Painter.fill(level, this, 2, Terrain.randomFloor());

		if (Random.chance(50)) {
			Painter.fill(level, this, 3, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloor());
		} else {
			Painter.fillEllipse(level, this, 3, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloor());
		}

		Point center = this.getCenter();
		Mimic chest = new Mimic();

		chest.x = center.x * 16;
		chest.y = center.y * 16;
		// chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);
	}

	@Override
	public boolean canConnect(Point p) {
		if (p.x == this.left + 1 && (p.y == this.top + 1 || p.y == this.bottom - 1)) {
			return false;
		}

		if (p.x == this.right - 1 && (p.y == this.top + 1 || p.y == this.bottom - 1)) {
			return false;
		}

		return super.canConnect(p);
	}

	@Override
	protected int validateWidth(int w) {
		return w % 2 == 0 ? w : w + 1;
	}

	@Override
	protected int validateHeight(int h) {
		return h % 2 == 0 ? h : h + 1;
	}
}