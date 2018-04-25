package org.rexellentgames.dungeon.entity.level.rooms.regular.ladder;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.chest.Chest;
import org.rexellentgames.dungeon.entity.level.entities.chest.WoodenChest;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.util.geometry.Point;

public class EntranceRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point point = this.getCenter();
		Painter.set(level, (int) point.x, (int) point.y, Terrain.WOOD);

		Entrance entrance = new Entrance();

		entrance.x = point.x * 16;
		entrance.y = point.y * 16 - 8;

		level.addSaveable(entrance);
		Dungeon.area.add(entrance);

		// Debug

		Chest chest = new WoodenChest();
		point = this.getRandomCell();

		chest.x = point.x * 16;
		chest.y = point.y * 16;

		Dungeon.area.add(chest);
		Dungeon.level.addSaveable(chest);
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}

	@Override
	public int getMinWidth() {
		return 5;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	@Override
	public int getMaxWidth() {
		return 9;
	}

	@Override
	public int getMaxHeight() {
		return 9;
	}
}