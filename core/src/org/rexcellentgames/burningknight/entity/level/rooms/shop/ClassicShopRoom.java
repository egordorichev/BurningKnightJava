package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class ClassicShopRoom extends ShopRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.randomFloor());

		if (Random.chance(70)) {
			if (Random.chance(50)) {
				Painter.fill(level, this, 2, Terrain.randomFloor());
			} else {
				Painter.fillEllipse(level, this, 2, Terrain.randomFloor());
			}
		}

		placeItems();
	}
}