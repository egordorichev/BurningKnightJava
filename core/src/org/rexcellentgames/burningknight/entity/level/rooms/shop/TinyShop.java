package org.rexcellentgames.burningknight.entity.level.rooms.shop;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.Level;

import java.util.ArrayList;

public class TinyShop extends ShopRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);


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