package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class ChestRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Terrain.randomFloor();
		byte fl = Random.chance(30) ? Terrain.WALL : (Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);
		Painter.fill(level, this, Terrain.WALL);

		if (fl == Terrain.LAVA) {
			f = Random.chance(40) ? Terrain.WATER : Terrain.DIRT;
		}

		Painter.fill(level, this, 1, Terrain.randomFloor());

		Painter.fill(level, this, 1, f);
		Painter.fill(level, this, 2, fl);

		boolean el = Random.chance(50);

		if (el) {
			Painter.fillEllipse(level, this, 3, f);
		} else {
			Painter.fill(level, this, 3, f);
		}

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

		if (Random.chance(50)) {
			byte flr = Terrain.randomFloor();

			if (Random.chance(50)) {
				Painter.fill(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.top + 4), flr);
				Painter.fill(level, new Rect(this.right - 3, this.top + 1, this.right, this.top + 4), flr);
				Painter.fill(level, new Rect(this.left + 1, this.bottom - 3, this.left + 4, this.bottom), flr);
				Painter.fill(level, new Rect(this.right - 3, this.bottom - 3, this.right, this.bottom), flr);
			} else {
				Painter.fillEllipse(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.top + 4), flr);
				Painter.fillEllipse(level, new Rect(this.right - 3, this.top + 1, this.right, this.top + 4), flr);
				Painter.fillEllipse(level, new Rect(this.left + 1, this.bottom - 3, this.left + 4, this.bottom), flr);
				Painter.fillEllipse(level, new Rect(this.right - 3, this.bottom - 3, this.right, this.bottom), flr);
			}
		}

		Chest chest = Chest.random();

		chest.x = this.left * 16 + (this.getWidth() * 16 - chest.w) / 2;
		chest.y = this.top * 16 + (this.getHeight() * 16 - chest.h) / 2;
		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);
	}

	@Override
	protected int validateWidth(int w) {
		return w % 2 == 0 ? w - 1 : w;
	}

	@Override
	protected int validateHeight(int h) {
		return h % 2 == 0 ? h - 1 : h;
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	@Override
	public int getMaxWidth() {
		return 12;
	}

	@Override
	public int getMaxHeight() {
		return 12;
	}
}