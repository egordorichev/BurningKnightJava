package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Slab;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.entity.pool.room.RegularRoomPool;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LampRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		RegularRoom room;

		do {
			room = RegularRoomPool.instance.generate();
		} while (room instanceof TrapRoom || room instanceof TableRoom || room instanceof CenterTableRoom || room instanceof MazeRoom || room instanceof BigHoleRoom);

		room.size = this.size;
		room.left = this.left;
		room.right = this.right;
		room.top = this.top;
		room.bottom = this.bottom;
		room.neighbours = this.neighbours;
		room.connected = this.connected;

		room.paint(level);

		Point center = this.getCenter();

		center.y -= 1;
		Painter.set(level, center, Terrain.FLOOR_A);
		center.y += 1;
		Painter.set(level, center, Terrain.FLOOR_A);

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
		return 6;
	}

	@Override
	public int getMinWidth() {
		return 6;
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