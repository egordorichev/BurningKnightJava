package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Statue;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;

public class StatueRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			this.collumn(this.left + 2);
			this.collumn(this.right - 2);
		} else {
			this.row(this.top + 2);
			this.row(this.bottom - 2);
		}

		if (Random.chance(50)) {
			byte f = Terrain.CHASM;

			if (Random.chance(50)) {
				Painter.fillEllipse(level, this, 3, f);
			} else {
				Painter.fill(level, this, 3, f);
			}

			if (Random.chance(50)) {
				f = Terrain.WALL;

				if (Random.chance(50)) {
					Painter.fillEllipse(level, this, 5, f);
				} else {
					Painter.fill(level, this, 5, f);
				}
			}
		}
	}

	private void collumn(int x) {
		for (int y = this.top + 2; y < this.bottom - 1; y += 3) {
			Statue statue = new Statue();

			statue.x = x * 16;
			statue.y = y * 16;

			Dungeon.area.add(statue);
			LevelSave.add(statue);
		}
	}

	private void row(int y) {
		for (int x = this.left + 2; x < this.right - 1; x += 2) {
			Statue statue = new Statue();

			statue.x = x * 16;
			statue.y = y * 16;

			Dungeon.area.add(statue);
			LevelSave.add(statue);
		}
	}

	@Override
	protected int validateWidth(int w) {
		return w - w % 2 + 1;
	}

	@Override
	protected int validateHeight(int h) {
		return h - h % 2 + 1;
	}
}