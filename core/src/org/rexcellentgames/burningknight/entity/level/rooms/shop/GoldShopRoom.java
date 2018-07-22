package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class GoldShopRoom extends ShopRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		boolean el = Random.chance(50);

		if (el) {
			Painter.fillEllipse(level, this, 1, Terrain.FLOOR_D);
		} else {
			Painter.fill(level, this, 1, Terrain.FLOOR_D);
		}

		el = el || Random.chance(50);

		if (el) {
			Painter.fillEllipse(level, this, 2, Terrain.randomFloor());
		} else {
			Painter.fill(level, this, 2, Terrain.randomFloor());
		}

		el = el || Random.chance(50);

		if (el) {
			Painter.fillEllipse(level, this, 3, Random.chance(40) ? Terrain.FLOOR_D : Terrain.randomFloor());
		} else {
			Painter.fill(level, this, 3, Random.chance(40) ? Terrain.FLOOR_D : Terrain.randomFloor());
		}

		Painter.drawLine(level, new Point(this.left + 1, this.bottom - 2), new Point(this.right - 1, this.bottom - 2),
			Random.chance(50) ? Terrain.CHASM : Terrain.LAVA);

		for (int i = 0; i < Random.newInt(3, 8); i++) {
			ItemHolder holder = new ItemHolder();
			holder.setItem(new Gold()).getItem().generate();

			holder.x = this.left * 16 + 16 + Random.newInt(this.getWidth() * 16 - 32);
			holder.y = (this.bottom - 1) * 16;

			Dungeon.area.add(holder);
			LevelSave.add(holder);
		}

		placeItems();
	}

	@Override
	protected Point getSpawn() {
		Point point;

		do {
			point = this.getRandomFreeCell();
		} while (point.y >= this.bottom - 2);

		return point;
	}

	@Override
	public int getMinHeight() {
		return 10;
	}

	@Override
	public int getMaxHeight() {
		return 11;
	}

	@Override
	public boolean canConnect(Point p) {
		if (p.y == this.top || p.y >= this.bottom - 2) {
			return false;
		}

		return super.canConnect(p);
	}
}