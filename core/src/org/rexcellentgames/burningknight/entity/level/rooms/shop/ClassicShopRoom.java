package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class ClassicShopRoom extends ShopRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.randomFloor());
		placeItems();
	}
}