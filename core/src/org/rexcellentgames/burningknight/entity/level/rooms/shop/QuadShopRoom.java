package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Rect;

import java.util.ArrayList;

public class QuadShopRoom extends ShopRoom {
	protected Rect quad;

	@Override
	public void paint(Level level) {
		super.paint(level);

		int v = Math.min(this.getWidth(), this.getHeight()) - 4;

		quad = new Rect(this.left + (this.getWidth() - v) / 2,
			this.top + (this.getHeight() - v) / 2,
			this.left + (this.getWidth() + v) / 2,
			this.top + (this.getHeight() + v) / 2);

		Painter.fill(level, quad, Terrain.FLOOR_D);

		if (Random.chance(70)) {
			if (Random.chance(50)) {
				if (Random.chance(50)) {
					Painter.fill(level, quad, 1, Terrain.CHASM);
				} else {
					Painter.fillEllipse(level, quad, 1, Terrain.CHASM);
				}
			} else {
				if (Random.chance(50)) {
					Painter.fill(level, quad, 1, Terrain.randomFloor());
				} else {
					Painter.fillEllipse(level, quad, 1, Terrain.randomFloor());
				}
			}
		}

		placeItems();
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	@Override
	public int getMaxWidth() {
		return 12;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	@Override
	public int getMaxHeight() {
		return 12;
	}

	@Override
	protected int getItemCount() {
		return 4;
	}

	@Override
	protected boolean quad() {
		return true;
	}

	@Override
	protected void placeItems(ArrayList<Item> items) {
		placeItem(items.get(0), quad.left * 16, quad.top * 16);
		placeItem(items.get(1), quad.right * 16 - 16, quad.top * 16);
		placeItem(items.get(2), quad.left * 16, quad.bottom * 16 - 16);
		placeItem(items.get(3), quad.right * 16 - 16, quad.bottom * 16 - 16);
	}
}