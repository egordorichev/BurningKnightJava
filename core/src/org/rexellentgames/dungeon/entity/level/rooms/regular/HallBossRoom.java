package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.boss.Boss;
import org.rexellentgames.dungeon.entity.creature.mob.boss.CrazyKing;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Patch;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class HallBossRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		boolean[] patch = Patch.generate(this.getWidth(), this.getHeight(), 0.4f, 4);

		for (int x = 1; x < this.getWidth() - 1; x++) {
			for (int y = 1; y < this.getHeight() - 1; y++) {
				if (patch[x + y * this.getWidth()]) {
					level.set(this.left + x, this.top + y, Terrain.WOOD);
				}
			}
		}

		Point center = this.getCenter();

		Boss boss = new CrazyKing();

		boss.x = center.x * 16 + (16 - boss.w) / 2;
		boss.y = center.y * 16 + (16 - boss.h) / 2;

		// chair?

		Dungeon.area.add(boss);
		Dungeon.level.addSaveable(boss);

		Painter.fill(level, new Rect().resize(5, 5).setPos(this.left + 5, this.top + 5), Terrain.WALL);
		Painter.fill(level, new Rect().resize(5, 5).setPos(this.left + 15, this.top + 5), Terrain.WALL);

		Painter.fill(level, new Rect().resize(5, 5).setPos(this.left + 5, this.top + 15), Terrain.WALL);
		Painter.fill(level, new Rect().resize(5, 5).setPos(this.left + 15, this.top + 15), Terrain.WALL);
	}

	@Override
	public int getMinWidth() {
		return 25;
	}

	@Override
	public int getMaxWidth() {
		return 26;
	}

	@Override
	public int getMinHeight() {
		return 25;
	}

	@Override
	public int getMaxHeight() {
		return 26;
	}
}