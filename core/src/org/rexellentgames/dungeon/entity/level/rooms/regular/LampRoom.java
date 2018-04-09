package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LampRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();

		ItemHolder holder = new ItemHolder();

		Painter.set(level, center, Terrain.WOOD); // todo: something better

		holder.x = center.x * 16;
		holder.y = center.y * 16;

		holder.setItem(new Lamp());

		Dungeon.level.addSaveable(holder);
		Dungeon.area.add(holder);
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