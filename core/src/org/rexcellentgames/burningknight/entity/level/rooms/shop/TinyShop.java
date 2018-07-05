package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class TinyShop extends ShopRoom {
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
		placeItem(items.get(0), (this.left + this.getWidth() / 2) * 16,
			(this.top + this.getHeight() / 2) * 16);
	}

	@Override
	protected boolean quad() {
		return true;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	@Override
	public int getMinWidth() {
		return 5;
	}

	@Override
	public int getMaxWidth() {
		return 8;
	}

	@Override
	public int getMaxHeight() {
		return 8;
	}

	@Override
	protected int getItemCount() {
		return 1;
	}
}