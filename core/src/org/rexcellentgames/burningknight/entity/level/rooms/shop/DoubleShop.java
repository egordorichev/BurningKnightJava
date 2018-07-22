package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class DoubleShop extends ShopRoom {
	private boolean vertical;

	public DoubleShop() {
		vertical = Random.chance(50);
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			Painter.fill(level, this, 2, Terrain.randomFloor());
		} else {
			Painter.fillEllipse(level, this, 2, Terrain.randomFloor());
		}

		placeItems();
	}

	protected void placeItems(ArrayList<Item> items) {
		placeItem(items.get(0), (this.left + 2) * 16,
			(this.top + 2) * 16);

		placeItem(items.get(1), (this.left + (vertical ? 2 : 4)) * 16,
			(this.top + (vertical ? 4 : 2)) * 16);
	}

	@Override
	public int getMinWidth() {
		return vertical ? 5 : 7;
	}

	@Override
	public int getMaxWidth() {
		return vertical ? 6 : 8;
	}

	@Override
	public int getMinHeight() {
		return vertical ? 7 : 5;
	}

	@Override
	public int getMaxHeight() {
		return vertical ? 8 : 6;
	}

	@Override
	protected int getItemCount() {
		return 2;
	}
}