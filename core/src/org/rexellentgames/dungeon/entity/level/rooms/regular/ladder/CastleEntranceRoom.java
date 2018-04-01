package org.rexellentgames.dungeon.entity.level.rooms.regular.ladder;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class CastleEntranceRoom extends EntranceRoom {
	public Point spawn;

	@Override
	public void paint(Level level) {
		Rect bottomTower = new Rect(this.left, this.top, this.left + 7,  this.top + 7);
		Rect topTower = new Rect(this.left, this.bottom - 6, this.left + 7, this.bottom + 1);

		this.spawn = new Point(10, Random.newInt(bottomTower.bottom + 1, topTower.top - 1));

		Painter.fill(level, new Rect(0, bottomTower.bottom + 1, this.left, topTower.top - 1), Terrain.WOOD);
		Painter.fill(level, this, 0, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.WOOD);

		Painter.fill(level, topTower, Terrain.WALL);
		Painter.fill(level, topTower, 1, Terrain.FLOOR);

		Painter.fill(level, bottomTower, Terrain.WALL);
		Painter.fill(level, bottomTower, 1, Terrain.FLOOR);

		Painter.drawLine(level, new Point(this.left, topTower.top - 2), new Point(this.left, bottomTower.bottom + 1),
			Terrain.FLOOR);

		int x = bottomTower.left + 6;
		int y = bottomTower.bottom - 2;

		Painter.set(level, x, y, Terrain.FLOOR);

		Door door = new Door(x, y, true);

		level.addSaveable(door);
		Dungeon.area.add(door);

		Camera.instance.clamp.add(bottomTower.left * 16 - 32);

		this.addEntrance(topTower.left + topTower.getWidth() / 2, topTower.top + topTower.getHeight() / 2, Entrance.CASTLE_ENTRANCE_OPEN);
		this.addEntrance(bottomTower.left + bottomTower.getWidth() / 2, bottomTower.top + bottomTower.getHeight() / 2, Entrance.CASTLE_ENTRANCE_CLOSED);

		for (int xx = topTower.left + 1; xx <= topTower.right - 2; xx++) {
			for (int yy = topTower.top + 1; yy <= topTower.bottom - 2; yy++) {
				ItemHolder holder = new ItemHolder();

				holder.x = xx * 16 + 4 + Random.newInt(8);
				holder.y = yy * 16 + 4 + Random.newInt(8) - 8;
				holder.setItem(new Gold().randomize());

				Dungeon.area.add(holder);
				Dungeon.level.addSaveable(holder);
			}
		}
	}

	private void addEntrance(int x, int y, byte type) {
		Entrance entrance = new Entrance();

		entrance.x = x * 16;
		entrance.y = y * 16;

		entrance.setType(type);

		Dungeon.level.addSaveable(entrance);
		Dungeon.area.add(entrance);
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		} else if (side == Connection.BOTTOM || side == Connection.TOP || side == Connection.RIGHT) {
			return 0;
		}

		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		} else if (side == Connection.BOTTOM || side == Connection.TOP || side == Connection.RIGHT) {
			return 0;
		}

		return 1;
	}

	@Override
	public int getMinWidth() {
		return 11;
	}

	@Override
	public int getMaxWidth() {
		return 12;
	}

	@Override
	public int getMinHeight() {
		return 20;
	}

	@Override
	public int getMaxHeight() {
		return 21;
	}
}