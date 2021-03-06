package org.rexcellentgames.burningknight.entity.level.rooms.item;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class ItemRoom extends RegularRoom {
	protected void placeItem(Point point) {
		Item item = null;

		if (item == null) {
			if (Dungeon.level.itemsToSpawn.size() == 0) {
				item = new Sword();
			} else {
				item = Dungeon.level.itemsToSpawn.get(Random.newInt(Dungeon.level.itemsToSpawn.size()));
			}
		} else {
			Dungeon.level.itemsToSpawn.remove(item);
		}

		Slab slab = new Slab();

		slab.x = point.x * 16;
		slab.y = point.y * 16 - 8;

		Dungeon.area.add(slab.add());

		ItemHolder holder = new ItemHolder(item);

		holder.getItem().generate();

		holder.x = point.x * 16 + (16 - holder.w) / 2;
		holder.y = point.y * 16 + (16 - holder.h) / 2 - 8;

		Dungeon.area.add(holder.add());
	}

	@Override
	protected int validateWidth(int w) {
		return w % 2 == 0 ? w : w + 1;
	}

	@Override
	protected int validateHeight(int h) {
		return h % 2 == 0 ? h : h + 1;
	}
}