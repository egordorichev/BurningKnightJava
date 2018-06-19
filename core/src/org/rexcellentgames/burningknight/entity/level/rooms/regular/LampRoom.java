package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.Lamp;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.room.LampRoomPool;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class LampRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		RegularRoom room = LampRoomPool.instance.generate();

		room.size = this.size;
		room.left = this.left;
		room.right = this.right;
		room.top = this.top;
		room.bottom = this.bottom;
		room.neighbours = this.neighbours;
		room.connected = this.connected;

		room.paint(level);

		Point center = this.getCenter();
		ItemHolder holder = new ItemHolder();

		Slab slab = new Slab();

		slab.x = center.x * 16 + 1;
		slab.y = center.y * 16 - 4;

		LevelSave.add(slab);
		Dungeon.area.add(slab);

		holder.x = center.x * 16 + 4;
		holder.y = center.y * 16;
		holder.depth = 1;

		holder.setItem(new Lamp());

		LevelSave.add(holder);
		Dungeon.area.add(holder);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.ENEMY);
		}
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMinWidth() {
		return 7;
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
}