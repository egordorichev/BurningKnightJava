package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class GiantShop extends ShopRoom {
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
		for (int i = 0; i < items.size(); i++) {
			placeItem(items.get(i), (this.left + i % (items.size() / 3) * 2 + 2) * 16 + 1,
				(this.top + 3 + (int) Math.floor(i / (items.size() / 3)) * 2) * 16 - 4);
		}
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
	protected int getItemCount() {
		return super.getItemCount() * 3;
	}
}