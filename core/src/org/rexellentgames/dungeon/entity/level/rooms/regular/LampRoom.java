package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Slab;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LampRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();

		ItemHolder holder = new ItemHolder();

		Slab slab = new Slab();

		slab.x = center.x * 16 + 1;
		slab.y = center.y * 16 - 4;

		Dungeon.level.addSaveable(slab);
		Dungeon.area.add(slab);

		holder.x = center.x * 16 + 4;
		holder.y = center.y * 16;
		holder.depth = 1;

		holder.setItem(new Lamp());

		Dungeon.level.addSaveable(holder);
		Dungeon.area.add(holder);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.ENEMY);
		}
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 2;
		}

		return 1;
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 2;
	}

	@Override
	public int getMaxHeight() {
		return 11;
	}

	@Override
	public int getMaxWidth() {
		return 11;
	}
}