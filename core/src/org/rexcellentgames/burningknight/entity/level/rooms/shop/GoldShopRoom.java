package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.level.Level;

public class GoldShopRoom extends ShopRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		placeItems();
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	@Override
	public int getMaxHeight() {
		return 9;
	}
}